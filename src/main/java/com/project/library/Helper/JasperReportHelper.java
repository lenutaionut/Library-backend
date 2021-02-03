package com.project.library.Helper;

import com.project.library.Service.BookService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class JasperReportHelper {

    @Autowired
    BookService bookService;

    public JasperPrint generatePdfReport() throws FileNotFoundException, JRException {

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bookService.generateReport());
        InputStream inputStream = this.getClass().getResourceAsStream("/reports/bookReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

        return jasperPrint;
    }
}
