package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskDescription;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskName;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import de.vfh.workhourstracker.reporting.application.services.ReportGeneratorService;
import de.vfh.workhourstracker.reporting.application.services.ReportingService;
import de.vfh.workhourstracker.reporting.domain.report.Report;
import de.vfh.workhourstracker.reporting.domain.report.events.ReportCreated;
import de.vfh.workhourstracker.reporting.infrastructure.repositories.ReportRepository;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.EndTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.StartTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimePeriod;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReportingServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TimeEntryRepository timeEntryRepository;

    @Mock
    private ReportGeneratorService reportGeneratorService;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportingService reportingService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void testCreateReport_NoProjectsFound() {
        // Arrange
        Long userId = 1L;
        Long projectId = 1L;

        // Stub the repository call to return an empty list
        when(projectRepository.findProjectByUserIdAndProjId(userId, projectId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<?> response = reportingService.createReport(userId, projectId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("An unexpected error occurred.", error.getMessage());
        assertEquals("report", error.getField());
        assertEquals("ERROR", error.getErrorCode());
    }

    @Test
    //simuliert den gesamten Ablauf von createReport
    void testCreateReport_EventPublished() throws Exception {
        Long userId = 1L;
        Long projectId = 1L;

        List<Project> projects = List.of(new Project(userId, new ProjectName("Test Project"), new ProjectDescription("Description"), new Deadline(LocalDateTime.now())));
        List<Task> tasks = List.of(new Task(projectId, new TaskName("Task1"), new TaskDescription("Description"), new Deadline(LocalDateTime.now())));
        List<TimeEntry> timeEntries = List.of(new TimeEntry(1L, new StartTime(LocalDateTime.now()), new EndTime(LocalDateTime.now().plusDays(3)), new TimePeriod(Duration.ofHours(2))));
        byte[] pdfContent = new byte[]{1, 2, 3};
        Report report = new Report(userId, projectId);

        // Mock repository and service behavior
        when(projectRepository.findProjectByUserIdAndProjId(userId, projectId)).thenReturn(projects);
        when(taskRepository.findByProjectId(projectId)).thenReturn(tasks);
        when(timeEntryRepository.findByTaskId(anyLong())).thenReturn(timeEntries);
        when(reportGeneratorService.createPdfReport(userId, projects, timeEntries, tasks)).thenReturn(pdfContent);
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        // Call the service method
        ResponseEntity<?> response =   reportingService.createReport(userId, projectId);
        EventLogger.logWarning("Error creating report: " + response);
        // Verify interactions - Es wird geprüft, ob die erwarteten Methodenaufrufe tatsächlich ausgeführt wurden
        verify(reportRepository, times(1)).save(any(Report.class));
        verify(eventPublisher, times(1)).publishEvent(any(ReportCreated.class));
    }

}
