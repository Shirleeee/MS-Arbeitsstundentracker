package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import de.vfh.workhourstracker.projectmanagement.domain.model.Project;
import de.vfh.workhourstracker.projectmanagement.domain.model.Task;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectManagementController {

    private final ProjectManagementService projectManagementService;

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectManagementController(ProjectManagementService projectManagementService, ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectManagementService = projectManagementService;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }
    @GetMapping("/project")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    @GetMapping("/task")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping("/submitProjectData")
    public Project submitProjectData(@RequestBody Project project) {
        projectRepository.save(project);
        return project;
    }
    @PostMapping("/submitTaskData")
    public Task submitTaskData(@RequestBody Task task) {

//        this.projectManagementService.createTask(task.getProjectId(), task.getName(), task.getDescription(), String.valueOf(task.getDeadline()));
        taskRepository.save(task);
     //TODO
        return  task;
    }
}
