package de.vfh.workhourstracker.projectmanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.common.ProjectManagementValidationUtils;
import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectCreated;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectUpdated;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
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
public class ProjectManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final ProjectRepository projectRepository;
    private final TimeEntryRepository timeEntryRepository;
    EventLogger eventLogger = new EventLogger();
    private static final String INVALID = "INVALID";


    private String generateProjectNotFoundMessage(Long projectId) {
        return "Project with ID " + projectId + " does not exist in database.";
    }



    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository, ApplicationEventPublisher eventPublisher, TimeEntryRepository timeEntryRepository) {
        this.projectRepository = projectRepository;
        this.eventPublisher = eventPublisher;
        this.timeEntryRepository = timeEntryRepository;
    }

    public ResponseEntity<Object> createProject(Long userId, String name, String description, LocalDateTime deadline) {
        List<ErrorResponse> validationErrors = validateProjectInputs(name, description, deadline);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(validationErrors);
        }

        // Neues Projekt erstellen
        Project project = new Project(userId, new ProjectName(name), new ProjectDescription(description), new Deadline(deadline));
        project = projectRepository.save(project);

        // Event veröffentlichen
        publishProjectCreatedEvent(project);

        return ResponseEntity.ok(project);
    }

    private void publishProjectCreatedEvent(Project project) {
        ProjectCreated event = new ProjectCreated(
                this,
                project.getUserId(),
                project.getName(),
                project.getDescription(),
                project.getDeadline()
        );
        eventPublisher.publishEvent(event);
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


    public ResponseEntity<Object> updateProject(Long projectId, String name, String description, LocalDateTime deadline) {
        Project existingProject = projectRepository.findById(projectId).orElse(null);
        if (existingProject == null) {
            return createErrorResponse(
                    generateProjectNotFoundMessage(projectId)
            );
        }

        List<ErrorResponse> validationErrors = validateProjectInputs(name, description, deadline);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(validationErrors);
        }

        updateProjectFields(existingProject, name, description, deadline);

        existingProject = projectRepository.save(existingProject);

        publishProjectUpdatedEvent(existingProject);

        return ResponseEntity.ok(existingProject);
    }

    private List<ErrorResponse> validateProjectInputs(String name, String description, LocalDateTime deadline) {
        return ProjectManagementValidationUtils.getErrorResponses(name, description, deadline, eventLogger, INVALID);
    }



    private void updateProjectFields(Project project, String name, String description, LocalDateTime deadline) {
        project.setName(new ProjectName(name));
        project.setDescription(new ProjectDescription(description));
        project.setDeadline(new Deadline(deadline));
    }

    private void publishProjectUpdatedEvent(Project project) {
        ProjectUpdated event = new ProjectUpdated(
                this,
                project.getUserId(),
                project.getName(),
                project.getDescription(),
                project.getDeadline()
        );
        eventPublisher.publishEvent(event);
    }

    private ResponseEntity<Object> createErrorResponse(String message) {
        ErrorResponse errorResponse = new ErrorResponse(message, "projectId", INVALID);
        eventLogger.logError(message);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    public ResponseEntity<Object> deleteProject(Long projectId) {
        if (projectRepository.findById(projectId).isPresent()) {
            projectRepository.deleteById(projectId);
            return ResponseEntity.ok().build();
        } else {

            eventLogger.logError(generateProjectNotFoundMessage(projectId));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(generateProjectNotFoundMessage(projectId), "projectId", INVALID));
        }

    }



}
