package com.project.library.Jobs;

import com.project.library.Helper.JasperReportHelper;
import com.project.library.Service.ReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JasperReportTask {

    @Autowired
    JasperReportHelper jasperReportHelper;

    @Autowired
    ReportService reportService;

    @Scheduled(cron = "*/10 * * * * *")
    public void generateReport() throws FileNotFoundException, JRException {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String dateTime = formatter.format(date);

        String fileName = "pdfReport " + dateTime + ".pdf";
        File file = new File("src/main/resources/reports/"+fileName);

        JasperPrint jasperPrint = jasperReportHelper.generatePdfReport();
        OutputStream os = new FileOutputStream(file);
        JasperExportManager.exportReportToPdfStream(jasperPrint, os);
        reportService.getByteArrayFromFile(file);
        System.out.println("Pdf file saved!");
    }

}