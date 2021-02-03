package com.project.library.Jobs;

import com.project.library.Helper.ExcelHelper;
import com.project.library.Service.BookService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;


@Component
public class ExportExcelTask {

    @Autowired
    ExcelHelper excelHelper;

    @Autowired
    BookService bookService;


    @Scheduled(fixedRateString = "5000")
    public void saveExcelInMemory() {

        Map<Integer, Object[]> data = bookService.exportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();

        excelHelper.writeExcel(data, workbook);

        try {
            FileOutputStream out = new FileOutputStream(new File("src/main/resources/excel/excelDocument.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Excel file saved!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
