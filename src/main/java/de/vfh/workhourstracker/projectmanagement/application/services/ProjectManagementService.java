package de.vfh.workhourstracker.projectmanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectCreated;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectDeleted;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectUpdated;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
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
public class ProjectManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final ProjectRepository projectRepository;
    private final TimeEntryRepository timeEntryRepository;

    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project with ID %s does not exist in database.";

    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository, ApplicationEventPublisher eventPublisher, TimeEntryRepository timeEntryRepository) {
        this.projectRepository = projectRepository;
        this.eventPublisher = eventPublisher;
        this.timeEntryRepository = timeEntryRepository;
    }

    public ResponseEntity<?> createProject(Long userId, String name, String description, LocalDateTime deadline) {
        ResponseEntity<?> validationResponse = ValidationUtils.validateObject(name, description, deadline);
        if (validationResponse != null) {
            return validationResponse;
        }

        Project project = new Project(userId, new ProjectName(name), new ProjectDescription(description), new Deadline(deadline));
        project = projectRepository.save(project);


        ProjectCreated event = new ProjectCreated(this, project.getUserId(), project.getName(), project.getDescription(), project.getDeadline());
        eventPublisher.publishEvent(event);

        return ResponseEntity.ok(project);
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Duration getTotalDurationOfProject(Long projectId) {
        return timeEntryRepository.findTimeEntriesByProjectId(projectId).stream()
                .map(TimeEntry::getTimePeriod)
                .map(TimePeriod::getTimePeriod)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public ResponseEntity<?> updateProject(Long projectId, String name, String description, LocalDateTime deadline) {
        Project existingProject = projectRepository.findById(projectId).orElse(null);
        if (existingProject == null) {
            EventLogger.logError(String.format(PROJECT_NOT_FOUND_MESSAGE, projectId));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(String.format(PROJECT_NOT_FOUND_MESSAGE, projectId), "projectId", "INVALID"));
        }

        ResponseEntity<?> validationResponse = ValidationUtils.validateObject(name, description, deadline);
        if (validationResponse != null) {
            return validationResponse;
        }

        existingProject.setName(new ProjectName(name));
        existingProject.setDescription(new ProjectDescription(description));
        existingProject.setDeadline(new Deadline(deadline));

        existingProject = projectRepository.save(existingProject);

        ProjectUpdated event = new ProjectUpdated(this, existingProject.getUserId(), existingProject.getName(), existingProject.getDescription(), existingProject.getDeadline());
        eventPublisher.publishEvent(event);

        return ResponseEntity.ok(existingProject);
    }

    public ResponseEntity<?> deleteProject(Long projectId) {
        if (projectRepository.findById(projectId).isPresent()) {
            projectRepository.deleteById(projectId);

            ProjectDeleted event = new ProjectDeleted(this, projectId);
            eventPublisher.publishEvent(event);

            return ResponseEntity.ok().build();
        } else {
            EventLogger.logError(String.format(PROJECT_NOT_FOUND_MESSAGE, projectId));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(String.format(PROJECT_NOT_FOUND_MESSAGE, projectId), "projectId", "INVALID"));
        }
    }
}
