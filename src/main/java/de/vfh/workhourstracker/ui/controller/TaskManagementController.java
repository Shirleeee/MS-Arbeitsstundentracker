package de.vfh.workhourstracker.ui.controller;

import de.vfh.workhourstracker.projectmanagement.application.services.TaskManagementService;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    @Autowired
    public TaskManagementController(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }

    @GetMapping("/task")
    public List<Task> getAllTasks() {
        return taskManagementService.findAllTasks();
    }

    @PostMapping("/submitTaskData")
    public ResponseEntity<Object> submitTaskData(@RequestBody Task task) {
        try {
            ResponseEntity<Object> response = taskManagementService.createTask(
                    task.getProjectId(),
                    task.getName().getTaskName(),
                    task.getDescription().getTaskDescription(),
                    task.getDeadline().getDeadline()
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

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

    }

    @PostMapping("/updateTaskData")
    public ResponseEntity<Object> updateTaskData(@RequestBody Task task) {

        try {
            ResponseEntity<Object> response = taskManagementService.updateTask(
                    task.getTask_id(),
                    task.getName().getTaskName(),
                    task.getDescription().getTaskDescription(),
                    task.getDeadline().getDeadline()
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            } else {

                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            List<ErrorResponse> errors = new ArrayList<>();

            errors.add(new ErrorResponse("Unexpected error", "general", "INTERNAL_SERVER_ERROR"));

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable Long id) {

        ResponseEntity<Object> response = taskManagementService.deleteTask(id);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }

}
