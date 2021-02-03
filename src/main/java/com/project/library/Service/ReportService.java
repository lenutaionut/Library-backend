package com.project.library.Service;

import com.project.library.Model.Report;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface ReportService {
    /**
     * get all reports
     *
     * @return the list of reports
     */
    List<Report> getReports();

    /**
     * get report with that id
     *
     * @param id
     * @return report
     */
    Report getReportById(Integer id);

    /**
     * get file for the report with id {@param reportId}
     *
     * @param reportId
     * @return report as a byte[] or null if doesn't exist
     */
    byte[] getReportFile(Integer reportId);

    /**
     * get the pdf file and store it in the database
     *
     * @param handledDocument
     * @return the file as byte[]
     */
    byte[] getByteArrayFromFile(final File handledDocument);

}
