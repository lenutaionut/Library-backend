package com.project.library.Controller;

import com.project.library.Enum.Status;
import com.project.library.Helper.ExcelHelper;
import com.project.library.Helper.JasperReportHelper;
import com.project.library.Message.ResponseMessage;
import com.project.library.Model.Book;
import com.project.library.Service.BookService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ResponseMessage responseMessage;

    @Autowired
    private ExcelHelper excelHelper;

    @Autowired
    private JasperReportHelper jasperReportHelper;

    @PostMapping(path = "/add")
    public ResponseEntity<ResponseMessage> addBook(@RequestParam String title, @RequestParam String author,
                                                   @RequestParam Integer pieces, @RequestParam Double price, @RequestParam MultipartFile image,
                                                   @RequestParam Status status) {
        String message = "";

        Integer savedId = bookService.addBook(title, author, pieces, price, image, status);
        message = responseMessage.getMessage();
        if (savedId == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
        return ResponseEntity.ok().body(new ResponseMessage(message));
    }

    @ResponseBody
    @GetMapping(path = "{bookId}/image")
    public byte[] getImageById(@PathVariable Integer bookId) {
        return bookService.getBookImage(bookId);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = bookService.getBooks();
        if (CollectionUtils.isEmpty(books)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(books);
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping(path = "/{bookId}")
    public ResponseEntity<Integer> deleteBook(@PathVariable Integer bookId) {
        Integer deletedBookId = bookService.deleteBook(bookId);
        if (deletedBookId == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(bookId);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Integer updatedId = bookService.updateBook(book);
        if (updatedId == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(book);

    }

    @PutMapping(path = "/buy/{bookId}")
    public ResponseEntity<Book> buyBook(@PathVariable Integer bookId) {
        Book boughtBook = bookService.buyBook(bookId);
        if (boughtBook == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(boughtBook);
    }

    @PutMapping(path = "/borrow/{bookId}")
    public ResponseEntity<Book> borrowBook(@PathVariable Integer bookId) {
        Book borrowedBook = bookService.borrowBook(bookId);
        if (borrowedBook == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(borrowedBook);
    }

    @PutMapping(path = "/return/{bookId}")
    public ResponseEntity<Book> returnBook(@PathVariable Integer bookId) {
        Book returnedBook = bookService.returnBook(bookId);
        if (returnedBook == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(returnedBook);
    }

    @GetMapping("/export")
    public void excelExport(HttpServletResponse response) {
        Map<Integer, Object[]> data = bookService.exportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        excelHelper.writeExcel(data, workbook);

        try {
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"excelRep.xlsx\""));
            OutputStream out = response.getOutputStream();

            workbook.write(out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            bookService.saveExcelDataToDB(file);
            message = responseMessage.getMessage() + " File: " + file.getOriginalFilename() + " uploaded successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/report")
    public void report(HttpServletResponse response) throws Exception {
        String fileType = "downloadPDF";

        JasperPrint jasperPrint = jasperReportHelper.generatePdfReport();

        if (fileType == "pdf") {
            response.setContentType("application/pdf");
            OutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } else if (fileType == "html") {
            response.setContentType("text/html");
            HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
            exporter.exportReport();
        } else if (fileType == "downloadPDF") {
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"Books.pdf\""));
            OutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        }
    }
}