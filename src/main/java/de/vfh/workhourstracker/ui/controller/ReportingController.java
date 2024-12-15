package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.reporting.application.services.ReportingService;
import de.vfh.workhourstracker.reporting.domain.report.Report;
import de.vfh.workhourstracker.reporting.domain.report.ReportSource;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportingController {

    private final ReportingService reportingService;

    @Autowired
    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @PostMapping("/reportAll/{userId}")
    public ResponseEntity<?> getReport(@PathVariable Long userId) throws Exception {

        ReportSource reportSource = new ReportSource();
        reportSource.setReportSource("reportSource");
        ResponseEntity<?> response = reportingService.createReport(userId, reportSource);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        } else {
            // Fehlerbehandlung basierend auf der Antwort
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

    }
}
