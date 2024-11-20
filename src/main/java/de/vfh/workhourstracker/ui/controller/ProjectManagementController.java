package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projectManagement")
public class ProjectManagementController {

    private final ProjectManagementService projectManagementService;

    @Autowired
    public ProjectManagementController(ProjectManagementService projectManagementService) {
        this.projectManagementService = projectManagementService;
    }

}
