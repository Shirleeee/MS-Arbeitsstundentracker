package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.Project;
import de.vfh.workhourstracker.entity.Task;
import de.vfh.workhourstracker.repository.ProjectRepository;
import de.vfh.workhourstracker.repository.TaskRepository;
import de.vfh.workhourstracker.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ProjectManagementService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    EventLogger eventLogger = new EventLogger();

    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;

    }

    //region project
    public Project createProject(String name, String description, String deadline) {

        validateName(name);
        validateDescription(description);
        LocalDateTime dateTimeDeadline = validateDeadline(deadline);

        Project newProject = this.saveProject(new Project(name, description, dateTimeDeadline));

        return newProject;
    }

    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
    //endregion

    //region task
    public Task createTask(Long projectId, String name, String description, String deadline) {

        validateName(name);
        validateDescription(description);
        LocalDateTime dateTimeDeadline = validateDeadline(deadline);

        Task newTask = this.saveTask(new Task(projectId, name, description, dateTimeDeadline));
        return newTask;
    }

    public Task findTask(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
    //endregion

    //region validation
    public String validateName(String name) {
        if (name == null || name.isEmpty()) {
            eventLogger.logWarning("Name darf nicht leer sein.");
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


    public LocalDateTime validateDeadline(String deadline) {
        if (deadline == null) {
            eventLogger.logWarning("Deadline darf nicht leer sein.");
            return null;
        } else {
            String deadlinePattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}";

            if (!deadline.matches(deadlinePattern)) {
                eventLogger.logWarning("Deadline ist nicht valide.");
                return null;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dateTimeDeadline = LocalDateTime.parse(deadline, formatter);

            LocalDateTime now = LocalDateTime.now();

            if (!now.isBefore(dateTimeDeadline)) {
                eventLogger.logWarning("Deadline liegt in der Vergangenheit");
                return null;
            }
            return dateTimeDeadline;
        }
    }
    //endregion
}
