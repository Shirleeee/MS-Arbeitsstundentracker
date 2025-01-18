package de.vfh.workhourstracker.projectmanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskDescription;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskName;
import de.vfh.workhourstracker.projectmanagement.domain.task.events.TaskCreated;
import de.vfh.workhourstracker.projectmanagement.domain.task.events.TaskDeleted;
import de.vfh.workhourstracker.projectmanagement.domain.task.events.TaskUpdated;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import de.vfh.workhourstracker.projectmanagement.utils.ValidationUtils;
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
import java.util.List;

@Service
public class TaskManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final TaskRepository taskRepository;
    private final TimeEntryRepository timeEntryRepository;

    private static final String TASK_NOT_FOUND_MESSAGE = "Task with ID %s does not exist in database.";

    @Autowired
    public TaskManagementService(ApplicationEventPublisher eventPublisher, TaskRepository taskRepository, TimeEntryRepository timeEntryRepository) {
        this.eventPublisher = eventPublisher;
        this.taskRepository = taskRepository;
        this.timeEntryRepository = timeEntryRepository;
    }

    public ResponseEntity<?> createTask(Long projectId, String name, String description, LocalDateTime deadline) {
        ResponseEntity<?> validationResponse = ValidationUtils.validateObject(name, description, deadline);
        if (validationResponse != null) {
            return validationResponse;
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

    public Duration getTotalDurationOfTask(Long taskId) {
        return timeEntryRepository.findByTaskId(taskId).stream()
                .map(TimeEntry::getTimePeriod)
                .map(TimePeriod::getTimePeriod)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public ResponseEntity<?> updateTask(Long taskId, String name, String description, LocalDateTime deadline) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);
        if (existingTask == null) {
            EventLogger.logError(String.format(TASK_NOT_FOUND_MESSAGE, taskId));
            return null;
        }

        ResponseEntity<?> validationResponse = ValidationUtils.validateObject(name, description, deadline);
        if (validationResponse != null) {
            return validationResponse;
        }

        existingTask.setName(new TaskName(name));
        existingTask.setDescription(new TaskDescription(description));
        existingTask.setDeadline(new Deadline(deadline));

        existingTask = taskRepository.save(existingTask);

        TaskUpdated event = new TaskUpdated(this, existingTask.getTask_id(), existingTask.getProjectId(), existingTask.getName(), existingTask.getDescription(), existingTask.getDeadline());
        eventPublisher.publishEvent(event);

        return ResponseEntity.ok(existingTask);
    }

    public ResponseEntity<?> deleteTask(Long taskId) {
        if (taskRepository.findById(taskId).isPresent()) {
            taskRepository.deleteById(taskId);

            TaskDeleted event = new TaskDeleted(this, taskId);
            eventPublisher.publishEvent(event);

            return ResponseEntity.ok().build();
        } else {
            EventLogger.logError(String.format(TASK_NOT_FOUND_MESSAGE, taskId));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(String.format(TASK_NOT_FOUND_MESSAGE, taskId), "taskId", "INVALID"));
        }

    }
}




