package de.vfh.workhourstracker.projectmanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.common.ProjectManagementValidationUtils;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskDescription;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskName;
import de.vfh.workhourstracker.projectmanagement.domain.task.events.TaskCreated;
import de.vfh.workhourstracker.projectmanagement.domain.task.events.TaskUpdated;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimePeriod;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class TaskManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final TaskRepository taskRepository;
    private final TimeEntryRepository timeEntryRepository;
    EventLogger eventLogger = new EventLogger();
    private static final String INVALID = "INVALID";

    private String generateTaskNotFoundMessage(Long projectId) {
        return "Task with ID " + projectId + " does not exist in database.";
    }

    @Autowired
    public TaskManagementService(ApplicationEventPublisher eventPublisher, TaskRepository taskRepository, TimeEntryRepository timeEntryRepository) {
        this.eventPublisher = eventPublisher;
        this.taskRepository = taskRepository;
        this.timeEntryRepository = timeEntryRepository;
    }

    public ResponseEntity<Object> createTask(Long projectId, String name, String description, LocalDateTime deadline) {
        List<ErrorResponse> validationErrors = validateTaskInputs(name, description, deadline);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(validationErrors);
        }

        // Neues Task erstellen
        Task task = new Task(projectId, new TaskName(name), new TaskDescription(description), new Deadline(deadline));
        task = taskRepository.save(task);

        // Event veröffentlichen
        publishTaskCreatedEvent(task);

        return ResponseEntity.ok(task);

    }

    private void publishTaskCreatedEvent(Task task) {
        TaskCreated event = new TaskCreated(
                this,
                task.getTask_id(),
                task.getProjectId(),
                task.getName(),
                task.getDescription(),
                task.getDeadline()
        );
        eventPublisher.publishEvent(event);
    }


    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Duration getTotalDurationOfTask(Long taskId) {
        return timeEntryRepository.findByTaskId(taskId).stream()
                .map(TimeEntry::getTimePeriod)
                .map(TimePeriod::getTimePeriod)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public ResponseEntity<Object> updateTask(Long taskId, String name, String description, LocalDateTime deadline) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);
        if (existingTask == null) {
            eventLogger.logError(generateTaskNotFoundMessage(taskId));
            return null;
        }

        // Validierung der Eingabedaten
        List<ErrorResponse> validationErrors = validateTaskInputs(name, description, deadline);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(validationErrors);
        }

        // Projekt aktualisieren
        updateTaskFields(existingTask, name, description, deadline);

        // Änderungen speichern
        existingTask = taskRepository.save(existingTask);

        // Event veröffentlichen
        publishTaskUpdatedEvent(existingTask);

        return ResponseEntity.ok(existingTask);

    }

    private List<ErrorResponse> validateTaskInputs(String name, String description, LocalDateTime deadline) {
        return ProjectManagementValidationUtils.getErrorResponses(name, description, deadline, eventLogger, INVALID);
    }

    private void updateTaskFields(Task task, String name, String description, LocalDateTime deadline) {
        task.setName(new TaskName(name));
        task.setDescription(new TaskDescription(description));
        task.setDeadline(new Deadline(deadline));
    }

    private void publishTaskUpdatedEvent(Task task) {

        TaskUpdated event = new TaskUpdated(
                this,
                task.getTask_id(),
                task.getProjectId(),
                task.getName(),
                task.getDescription(),
                task.getDeadline()
        );
        eventPublisher.publishEvent(event);
    }

    public ResponseEntity<Object> deleteTask(Long taskId) {
        if (taskRepository.findById(taskId).isPresent()) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.ok().build();
        } else {

            eventLogger.logError(generateTaskNotFoundMessage(taskId));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(generateTaskNotFoundMessage(taskId), "taskId", INVALID));
        }

    }

    //region validation


//endregion
}




