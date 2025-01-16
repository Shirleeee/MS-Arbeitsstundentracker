package de.vfh.workhourstracker.reporting.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectId;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import de.vfh.workhourstracker.reporting.domain.report.Report;
import de.vfh.workhourstracker.reporting.domain.report.events.ReportCreated;
import de.vfh.workhourstracker.reporting.infrastructure.repositories.ReportRepository;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.shared.util.ResourceNotFoundException;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportingService {
    private final ApplicationEventPublisher eventPublisher;
    private final ReportRepository reportRepository;
    private final TimeEntryRepository timeEntryRepository;
    private final TaskRepository taskRepository;
    EventLogger eventLogger = new EventLogger();
    private final ProjectRepository projectRepository;
    private final ReportGeneratorService reportGeneratorService;

    @Autowired
    public ReportingService(ApplicationEventPublisher eventPublisher, ReportRepository reportRepository, ProjectRepository projectRepository, TimeEntryRepository timeEntryRepository, TaskRepository taskRepository, ReportGeneratorService reportGeneratorService) {
        this.eventPublisher = eventPublisher;
        this.reportRepository = reportRepository;
        this.projectRepository = projectRepository;
        this.timeEntryRepository = timeEntryRepository;
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<?> createReport(Long userId, Long projectId) {
        try {
            List<Project> projects = fetchProjects(userId, projectId);
            List<Task> tasks = fetchTasks(projectId);
            List<TimeEntry> timeEntries = fetchTimeEntries(tasks);
            byte[] pdfContent = reportGeneratorService.createPdfReport(userId, projects, timeEntries, tasks);
            Report report = saveReport(userId, projectId);
            publishReportEvent(report, projectId);
            return createPdfResponse(userId, pdfContent);
        } catch (Exception e) {
            return handleReportError(e);
        }
    }


    private List<Project> fetchProjects(Long userId, Long projectId) {
        List<Project> projects = (projectId != 0)
                ? projectRepository.findProjectByUserIdAndProjId(userId, projectId)
                : projectRepository.findProjectByUserId(userId);
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No projects found for user.");
        }
        return projects;
    }

    private List<Task> fetchTasks(Long projectId) {
        List<Task> tasks = (projectId != 0)
                ? taskRepository.findByProjectId(projectId)
                : taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("No tasks found for the project.");
        }
        return tasks;
    }

    private List<TimeEntry> fetchTimeEntries(List<Task> tasks) {
        return tasks.stream()
                .map(task -> timeEntryRepository.findByTaskId(task.getTask_id()))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private ResponseEntity<?> handleReportError(Exception e) {
        eventLogger.logError("Error creating report: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An unexpected error occurred.", "report", "ERROR"));
    }


    private Report saveReport(Long userId, Long projectId) {
        Report report = new Report(new UserId(userId), new ProjectId(projectId));
        return reportRepository.save(report);
    }

    private void publishReportEvent(Report report, Long projectId) {
        ReportCreated event = new ReportCreated(this, report.getUserId(), projectId);
        eventPublisher.publishEvent(event);
    }

    private ResponseEntity<byte[]> createPdfResponse(Long userId, byte[] pdfContent) {
        if (pdfContent == null) {
            eventLogger.logError("PDF content is null.");
            throw new IllegalStateException("PDF could not be created.");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report-" + userId + ".pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(pdfContent);
    }


}
