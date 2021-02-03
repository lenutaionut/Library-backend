package com.project.library.Helper;

import com.project.library.Enum.Status;
import com.project.library.Message.ResponseMessage;
import com.project.library.Model.Book;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class ExcelHelper {
    static String SHEET = "Book Data";

    @Autowired
    ResponseMessage responseMessage;

    @Autowired
    FirebaseHelper firebaseHelper;

    public void writeExcel(Map<Integer, Object[]> data, XSSFWorkbook workbook) {

        XSSFSheet sheet = workbook.createSheet("Book Data");

        Set<Integer> keyset = data.keySet();
        int rownum = 0;
        for (Integer key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
                else if (obj instanceof Status)
                    cell.setCellValue(obj.toString());
            }
        }
    }

    public List<Book> excelToBooks(InputStream inputStream) {
        try {
            responseMessage.setMessage("");
            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Book> books = new ArrayList<>();

            int rowIndex = 0;
            start:
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Book book = new Book();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIndex) {
                        case 0:
                            if (currentCell.getCellType() != Cell.CELL_TYPE_STRING) {
                                responseMessage.setMessage(responseMessage.getMessage() + " Error on row: " + rowIndex + "! Title must be a string\n");
                                rowIndex++;
                                continue start;
                            }
                            book.setTitle(currentCell.getStringCellValue());
                            break;
                        case 1:
                            if (currentCell.getCellType() != Cell.CELL_TYPE_STRING) {
                                responseMessage.setMessage(responseMessage.getMessage() + " Error on row: " + rowIndex + "! Author must be a string\n");
                                rowIndex++;
                                continue start;
                            }
                            book.setAuthor(currentCell.getStringCellValue());
                            break;
                        case 2:
                            if (currentCell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                                responseMessage.setMessage(responseMessage.getMessage() + " Error on row: " + rowIndex + "! Price must be a numeric value\n");
                                rowIndex++;
                                continue start;
                            }
                            book.setPrice(currentCell.getNumericCellValue());
                            break;
                        case 3:
                            if (currentCell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                                responseMessage.setMessage(responseMessage.getMessage() + " Error on row: " + rowIndex + "! Number of books must be a numeric value\n");
                                rowIndex++;
                                continue start;
                            }
                            book.setPieces((int) currentCell.getNumericCellValue());
                            break;
                        case 4:
                            book.setStatus(Status.valueOf(currentCell.getStringCellValue().toUpperCase()));
                            break;
                        case 5:
                            try {
                                byte[] image = firebaseHelper.downloadFile(currentCell.getStringCellValue());
                                book.setImage(image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                    cellIndex++;
                }
                books.add(book);
                rowIndex++;
            }
            return books;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
