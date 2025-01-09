package de.vfh.workhourstracker.projectmanagement.application.services;

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

    private static final LocalDateTime MAX_END_TIME = LocalDateTime.of(2100, 12, 31, 23, 59, 59);

    @Autowired
    public TaskManagementService(ApplicationEventPublisher eventPublisher, TaskRepository taskRepository, TimeEntryRepository timeEntryRepository) {
        this.eventPublisher = eventPublisher;
        this.taskRepository = taskRepository;
        this.timeEntryRepository = timeEntryRepository;
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

    public Duration getTotalDurationOfTask(Long taskId) {
        return timeEntryRepository.findByTaskId(taskId).stream()
                .map(TimeEntry::getTimePeriod)
                .map(TimePeriod::getTimePeriod)
                .reduce(Duration.ZERO, Duration::plus);
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


    public ResponseEntity<?> deleteTask(Long taskId) {
        if (taskRepository.findById(taskId).isPresent()) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.ok().build();
        } else {

            eventLogger.logError("Task with ID " + taskId + " does not exist in database.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse("Task with ID " + taskId + " does not exist in database.", "taskId", "INVALID"));
        }

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
        //TODO: Darf die Description nicht leer sein?
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
        if (deadline.isAfter(MAX_END_TIME)) {
            //TODO: Datum im String durch MAX_END_TIME ersetzen
            eventLogger.logWarning("Deadline darf nicht nach dem 31.12.2100 liegen.");
            return "Deadline darf nicht nach dem 31.12.2100 liegen.";
        }
        return "";
    }
    //endregion
}




