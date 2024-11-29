package de.vfh.workhourstracker.projectmanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectId;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectCreated;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectUpdated;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final ProjectRepository projectRepository;
    EventLogger eventLogger = new EventLogger();

    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository, TaskRepository taskRepository, ApplicationEventPublisher eventPublisher) {
        this.projectRepository = projectRepository;
        this.eventPublisher = eventPublisher;
    }

    public Project createProject(UserId userId, String name, String description, LocalDateTime deadline) {
        String validName = validateName(name);
        String validDescription = validateDescription(description);
        LocalDateTime validDeadline = validateDeadline(deadline);

        if (validName == null || validDescription == null || validDeadline == null) {
            eventLogger.logError("Project could not be created because of invalid input.");
            return null;
        }

        Project project = new Project(userId, new ProjectName(validName), new ProjectDescription(validDescription), new Deadline(validDeadline));
        project = projectRepository.save(project);

        ProjectCreated event = new ProjectCreated(this, project.getId(),project.getUserId(), project.getName(), project.getDescription(), project.getDeadline());
        eventPublisher.publishEvent(event);

        return project;
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Project updateProject(Long projectId, UserId userId, String name, String description, LocalDateTime deadline) {
        Project existingProject = projectRepository.findById(projectId).orElse(null);
        if (existingProject == null) {
            eventLogger.logError("Project with ID " + projectId + " does not exist in database.");
            return null;
        }

        String validName = validateName(name);
        String validDescription = validateDescription(description);
        LocalDateTime validDeadline = validateDeadline(deadline);

        if (validName == null || validDescription == null || validDeadline == null) {
            eventLogger.logError("Project could not be updated because of invalid input.");
            return null;
        }

        existingProject.setName(new ProjectName(validName));
        existingProject.setDescription(new ProjectDescription(validDescription));
        existingProject.setDeadline(new Deadline(validDeadline));

        existingProject = projectRepository.save(existingProject);

        ProjectUpdated event = new ProjectUpdated(this, existingProject.getId(), existingProject.getUserId(), existingProject.getName(), existingProject.getDescription(), existingProject.getDeadline());
        eventPublisher.publishEvent(event);

        return existingProject;
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
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
