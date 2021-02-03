package com.project.library.Controller;


import com.project.library.Model.Report;
import com.project.library.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/reports")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> getReports() {
        List<Report> reports = reportService.getReports();
        if (CollectionUtils.isEmpty(reports)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(reports);
    }

    @ResponseBody
    @GetMapping(path = "{reportId}")
    public byte[] getFileById(@PathVariable Integer reportId) {
        return reportService.getReportFile(reportId);
    }

}
