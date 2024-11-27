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
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Task createTask(ProjectId projectId, String name, String description, LocalDateTime deadline) {
        String validName = validateName(name);
        String validDescription = validateDescription(description);
        LocalDateTime validDeadline = validateDeadline(deadline);

        if (validName == null || validDescription == null || validDeadline == null) {
            eventLogger.logError("Project could not be created because of invalid input.");
            return null;
        }

        Task task = new Task(projectId, new TaskName(name), new TaskDescription(description), new Deadline(validDeadline));
        task = taskRepository.save(task);

        TaskCreated event = new TaskCreated(this, task.getId(), task.getProjectId(), task.getName(), task.getDescription(), task.getDeadline());
        eventPublisher.publishEvent(event);

        return task;
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task findTask(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(TaskId taskId, ProjectId projectId, String name, String description, LocalDateTime deadline) {
        //TODO: wie macht man das am besten mit der ID?
        Task existingTask = taskRepository.findById(taskId.getTaskId()).orElse(null);
        if (existingTask == null) {
            eventLogger.logError("Task with ID " + taskId.getTaskId() + " does not exist in database.");
            return null;
        }

        String validName = validateName(name);
        String validDescription = validateDescription(description);
        LocalDateTime validDeadline = validateDeadline(deadline);

        if (validName == null || validDescription == null || validDeadline == null) {
            eventLogger.logError("Task could not be updated because of invalid input.");
            return null;
        }

        existingTask.setName(new TaskName(name));
        existingTask.setDescription(new TaskDescription(description));
        existingTask.setDeadline(new Deadline(validDeadline));

        existingTask = taskRepository.save(existingTask);

        TaskUpdated event = new TaskUpdated(this, existingTask.getId(), existingTask.getProjectId(), existingTask.getName(), existingTask.getDescription(), existingTask.getDeadline());
        eventPublisher.publishEvent(event);

        return existingTask;
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    //region validation
    public String validateName(String name) {
        if (name == null || name.isEmpty()) {
            eventLogger.logWarning("Name darf nicht leer sein.");
            return null;
        }
        if (name.length() > 256) {
            eventLogger.logWarning("Name ist zu lang");
            return null;
        }
        return name;
    }

    public String validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            eventLogger.logWarning("Beschreibung darf nicht leer sein.");
            return null;
        }
        if (description.length() > 1024) {
            eventLogger.logWarning("Beschreibung ist zu lang.");
            return null;
        }
        return description;
    }

    public LocalDateTime validateDeadline(LocalDateTime deadline) {
        if (deadline == null) {
            eventLogger.logWarning("Deadline darf nicht leer sein.");
            return null;
        }
        if (!LocalDateTime.now().isBefore(deadline)) {
            eventLogger.logWarning("Deadline liegt in der Vergangenheit");
            return null;
        }
        return deadline;

    }
    //endregion
}




