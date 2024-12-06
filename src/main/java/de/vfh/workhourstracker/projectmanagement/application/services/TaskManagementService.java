package de.vfh.workhourstracker.projectmanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectId;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskDescription;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskId;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskName;
import de.vfh.workhourstracker.projectmanagement.domain.task.events.TaskCreated;
import de.vfh.workhourstracker.projectmanagement.domain.task.events.TaskUpdated;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final TaskRepository taskRepository;
    EventLogger eventLogger = new EventLogger();

    @Autowired
    public TaskManagementService(ApplicationEventPublisher eventPublisher, TaskRepository taskRepository) {
        this.eventPublisher = eventPublisher;
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<?> createTask(Long projectId, String name, String description, LocalDateTime deadline) {
        String validName = validateName(name);
        String validDescription = validateDescription(description);
        String validDeadline = validateDeadline(deadline);


        if (!validName.isEmpty() || !validDescription.isEmpty() || !validDeadline.isEmpty()) {

            List<ErrorResponse> errors = new ArrayList<>();
            if (!validName.isEmpty()) {
                errors.add(new ErrorResponse(validName, "name", "INVALID"));
            }
            if (!validDescription.isEmpty()) {
                errors.add(new ErrorResponse(validDescription, "description", "INVALID"));
            }
            if (!validDeadline.isEmpty()) {
                errors.add(new ErrorResponse(validDeadline, "deadline", "INVALID"));
            }
            // Rückgabe der Fehlerantwort
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

        Task task = new Task(projectId, new TaskName(name), new TaskDescription(description), new Deadline(deadline));
        task = taskRepository.save(task);

        TaskCreated event = new TaskCreated(this, task.getTask_id(), task.getProjectId(), task.getName(), task.getDescription(), task.getDeadline());
        eventPublisher.publishEvent(event);

        return ResponseEntity.ok(task);

    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public ResponseEntity<?> updateTask(Long taskId, String name, String description, LocalDateTime deadline) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);
        if (existingTask == null) {
            eventLogger.logError("Task with ID " + taskId + " does not exist in database.");
            return null;
        }

        String validName = validateName(name);
        String validDescription = validateDescription(description);
        String validDeadline = validateDeadline(deadline);

        if (!validName.isEmpty() || !validDescription.isEmpty() || !validDeadline.isEmpty()) {
            eventLogger.logError("Task could not be updated because of invalid input.");

            List<ErrorResponse> errors = new ArrayList<>();
            if (!validName.isEmpty()) {

                errors.add(new ErrorResponse(validName, "name", "INVALID"));
            }
            if (!validDescription.isEmpty()) {
                errors.add(new ErrorResponse(validDescription, "description", "INVALID"));
            }
            if (!validDeadline.isEmpty()) {
                errors.add(new ErrorResponse(validDeadline, "deadline", "INVALID"));
            }
            // Rückgabe der Fehlerantwort
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }
        existingTask.setName(new TaskName(name));
        existingTask.setDescription(new TaskDescription(description));
        existingTask.setDeadline(new Deadline(deadline));

        existingTask = taskRepository.save(existingTask);

        TaskUpdated event = new TaskUpdated(this, existingTask.getTask_id(), existingTask.getProjectId(), existingTask.getName(), existingTask.getDescription(), existingTask.getDeadline());
        eventPublisher.publishEvent(event);

        return ResponseEntity.ok(existingTask);

    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    //region validation
    public String validateName(String name) {
        if (name == null || name.isEmpty()) {
            eventLogger.logWarning("Name darf nicht leer sein.");
            return "Name darf nicht leer sein.";
        }
        if (name.length() > 256) {
            eventLogger.logWarning("Name ist zu lang");
            return "Name ist zu lang";
        }
        return "";
    }

    public String validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            eventLogger.logWarning("Beschreibung darf nicht leer sein.");
            return "Beschreibung darf nicht leer sein.";
        }
        if (description.length() > 1024) {
            eventLogger.logWarning("Beschreibung ist zu lang.");
            return "Beschreibung ist zu lang.";
        }
        return "";
    }

    public String validateDeadline(LocalDateTime deadline) {
        if (deadline == null) {
            eventLogger.logWarning("Deadline darf nicht leer sein.");
            return "Deadline darf nicht leer sein.";
        }
        if (!LocalDateTime.now().isBefore(deadline)) {
            eventLogger.logWarning("Deadline liegt in der Vergangenheit");
            return "Deadline liegt in der Vergangenheit";
        }
        return "";

    }
    //endregion
}




