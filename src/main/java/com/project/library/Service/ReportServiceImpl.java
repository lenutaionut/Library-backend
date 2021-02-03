package com.project.library.Service;

import com.project.library.DAO.ReportDAO;
import com.project.library.Model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportDAO reportDAO;

    @Override
    public List<Report> getReports() {
        return (List<Report>) reportDAO.findAll();
    }

    @Override
    public Report getReportById(Integer id) {
        return reportDAO.findById(id).orElse(null);
    }

    @Override
    public byte[] getReportFile(Integer reportId) {
        Report report = getReportById(reportId);
        if (report == null) {
            return null;
        }
        return report.getFile();
    }

    @Override
    public byte[] getByteArrayFromFile(final File handledDocument) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final InputStream in;
        try {
            in = new FileInputStream(handledDocument);

            final byte[] buffer = new byte[500];

            int read = -1;
            while ((read = in.read(buffer)) > 0) {
                baos.write(buffer, 0, read);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Report newReport = new Report();
        newReport.setName(handledDocument.getName());
        newReport.setFile(baos.toByteArray());
        reportDAO.save(newReport);

        return baos.toByteArray();
    }
}
