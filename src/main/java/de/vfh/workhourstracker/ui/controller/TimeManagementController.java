package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import de.vfh.workhourstracker.timemanagement.application.services.TimeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TimeManagementController {
    private final TimeManagementService timeManagementService;

    @Autowired
    public TimeManagementController(TimeManagementService timeManagementService, TimeEntryRepository timeEntryRepository) {
        this.timeManagementService = timeManagementService;
    }

    @GetMapping("/time_entries")
    public List<TimeEntry> getAllTimeEntries() {
        return timeManagementService.findAllTimeEntries();
    }

    @PostMapping("/submit_startTime")
    public ResponseEntity<?> submitStartTime(@RequestBody TimeEntry timeEntry) {
        try {
            ResponseEntity<?> response = timeManagementService.startTimeTracking(
                    timeEntry.getTaskId(),
                    timeEntry.getStartTime().getStartTime()
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            } else {
                // Fehlerbehandlung basierend auf der Antwort
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            List<ErrorResponse> errors = new ArrayList<>();

            errors.add(new ErrorResponse("Unexpected error", "general", "INTERNAL_SERVER_ERROR"));

            // Exception weiterverarbeiten und in eine ResponseEntity mit Fehlern zurückgeben
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

    }

    @PostMapping("/submit_endTime")
    public ResponseEntity<?> submitEndTime(@RequestBody TimeEntry timeEntry) {
        try {
            ResponseEntity<?> response = timeManagementService.endTimeTracking(
                    timeEntry.getId(),
                    timeEntry.getEndTime().getEndTime()
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            } else {
                // Fehlerbehandlung basierend auf der Antwort
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            List<ErrorResponse> errors = new ArrayList<>();

            errors.add(new ErrorResponse("Unexpected error", "general", "INTERNAL_SERVER_ERROR"));

            // Exception weiterverarbeiten und in eine ResponseEntity mit Fehlern zurückgeben
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

    }

}
