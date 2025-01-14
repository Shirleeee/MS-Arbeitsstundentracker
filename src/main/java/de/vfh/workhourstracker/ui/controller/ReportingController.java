package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.reporting.application.services.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReportingController {

    private final ReportingService reportingService;

    @Autowired
    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @PostMapping("/export_report/{userId}/{projId}")
    public ResponseEntity<Object> getReport(@PathVariable Long userId, @PathVariable Long projId) {


        ResponseEntity<Object> response = reportingService.createReport(userId, projId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

    }
}
