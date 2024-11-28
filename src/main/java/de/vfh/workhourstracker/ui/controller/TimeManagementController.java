package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import de.vfh.workhourstracker.timemanagement.application.services.TimeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
