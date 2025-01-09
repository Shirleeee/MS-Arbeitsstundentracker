package de.vfh.workhourstracker.projectmanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;

import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectCreated;
import de.vfh.workhourstracker.projectmanagement.domain.project.events.ProjectUpdated;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimePeriod;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.vfh.workhourstracker.shared.util.ErrorResponse;

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

    private static final LocalDateTime MAX_END_TIME = LocalDateTime.of(2100, 12, 31, 23, 59, 59);

    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository, ApplicationEventPublisher eventPublisher, TimeEntryRepository timeEntryRepository) {
        this.projectRepository = projectRepository;
        this.eventPublisher = eventPublisher;
        this.timeEntryRepository = timeEntryRepository;
    }

    public ResponseEntity<?> createProject(Long userId, String name, String description, LocalDateTime deadline) {
        String validName = validateName(name);
        String validDescription = validateDescription(description);
        String validDeadline = validateDeadline(deadline);

        if (!validName.isEmpty() || !validDescription.isEmpty() || !validDeadline.isEmpty()) {
            // Erstelle eine Liste von Fehlern
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

    //TODO: connect with frontend
    public ResponseEntity<?> updateProject(Long projectId, String name, String description, LocalDateTime deadline) {
        Project existingProject = projectRepository.findById(projectId).orElse(null);
        if (existingProject == null) {
            eventLogger.logError("Project with ID " + projectId + " does not exist in database.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse("Project with ID " + projectId + " does not exist in database.", "projectId", "INVALID"));
        }

        String validName = validateName(name);
        String validDescription = validateDescription(description);
        String validDeadline = validateDeadline(deadline);

        if (!validName.isEmpty() || !validDescription.isEmpty() || !validDeadline.isEmpty()) {
            // Erstelle eine Liste von Fehlern
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
            return ResponseEntity.ok().build();
        } else {

            eventLogger.logError("Project with ID " + projectId + " does not exist in database.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse("Project with ID " + projectId + " does not exist in database.", "projectId", "INVALID"));
        }

    }

    //region validation
    public String validateName(String name) {
        if (name == null || name.isEmpty()) {
            eventLogger.logWarning("Name darf nicht leer sein.");
            return "Name darf nicht leer sein.";
        }
        if (name.length() > 256) {
            eventLogger.logWarning("Name ist zu lang.");
            return "Name ist zu lang.";
        }
        return "";
    }

    public String validateDescription(String description) {
        //TODO: Warum darf die Description nicht null sein?
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
            eventLogger.logWarning("Deadline darf nicht in der Vergangenheit liegen.");
            return "Deadline darf nicht in der Vergangenheit liegen.";
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
