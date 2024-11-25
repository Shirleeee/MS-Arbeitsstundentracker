package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.timemanagement.domain.model.TimeEntry;

import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import de.vfh.workhourstracker.timemanagement.application.services.TimeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;

@RestController
@RequestMapping("/api")
public class TimeManagementController {

    private final TimeManagementService timeManagementService;
    private final TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeManagementController(TimeManagementService timeManagementService, TimeEntryRepository timeEntryRepository) {
        this.timeManagementService = timeManagementService;
        this.timeEntryRepository = timeEntryRepository;
    }


    @GetMapping("/time_entries")
    public List<TimeEntry> getAllTimeEntries() {
        return timeEntryRepository.findAll();
    }
}
