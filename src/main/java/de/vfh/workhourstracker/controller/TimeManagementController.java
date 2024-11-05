package de.vfh.workhourstracker.controller;

import de.vfh.workhourstracker.service.TimeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeManagement")
public class TimeManagementController {

    private final TimeManagementService timeManagementService;

    @Autowired
    public TimeManagementController(TimeManagementService timeManagementService) {
        this.timeManagementService = timeManagementService;
    }

}
