package de.vfh.workhourstracker.reporting.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import de.vfh.workhourstracker.reporting.domain.report.Report;
import de.vfh.workhourstracker.reporting.domain.report.ReportSource;
import de.vfh.workhourstracker.reporting.domain.report.events.ReportCreated;
import de.vfh.workhourstracker.reporting.infrastructure.repositories.ReportRepository;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportingService {
    private final ApplicationEventPublisher eventPublisher;
    private final ReportRepository reportRepository;
    private final TimeEntryRepository timeEntryRepository;
    private final TaskRepository taskRepository;
    EventLogger eventLogger = new EventLogger();
    private final ProjectRepository projectRepository;

    @Autowired
    public ReportingService(ApplicationEventPublisher eventPublisher, ReportRepository reportRepository, ProjectRepository projectRepository, TimeEntryRepository timeEntryRepository, TaskRepository taskRepository) {
        this.eventPublisher = eventPublisher;
        this.reportRepository = reportRepository;
        this.projectRepository = projectRepository;
        this.timeEntryRepository = timeEntryRepository;
        this.taskRepository = taskRepository;
    }

    public byte[] createPdfReport(Long userId, List<Project> projects, List<TimeEntry> timeEntries, List<Task> tasks) throws IOException {
        // Neues PDF-Dokument erstellen
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Inhalt streamen
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Überschrift hinzufügen
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 18);
        contentStream.newLineAtOffset(50, 750); // Position x=50, y=750
        contentStream.showText("Report für Benutzer: " + userId);
        contentStream.endText();

        // Startposition für die Tabelle
        float yPosition = 720;

        // Daten der Projekte einfügen
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);





        // Projekte durchlaufen und in das PDF einfügen


        for (Project project : projects) {



            String projectName = (project.getName() != null && project.getName().getProjectName() != null)
                    ? project.getName().getProjectName().replace("\n", " ")
                    : "N/A";

            String projectDescription = (project.getDescription() != null && project.getDescription().getProjectDescription() != null)
                    ? project.getDescription().getProjectDescription().replace("\n", " ")
                    : "N/A";

            String projectDeadline = (project.getDeadline() != null && project.getDeadline().getDeadline() != null)
                    ? project.getDeadline().getDeadline().toString().replace("\n", " ")
                    : "N/A";


            contentStream.beginText();
            // Spaltenüberschrift
            contentStream.showText("Projekt Titel");
            contentStream.endText();

            yPosition -= 20; // Zeilenabstand
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText(" | " + projectName + " | ");
            contentStream.endText();

            // Spaltenüberschrift
            contentStream.beginText();
            contentStream.showText("Projekt Beschreibung ");
            contentStream.endText();

            yPosition -= 20; // Zeilenabstand
            // Spaltenüberschrift
            contentStream.beginText();
            contentStream.showText("Projekt Deadline");
            contentStream.endText();

            yPosition -= 20; // Move to the next line
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText(projectDescription + " | " + projectDeadline);
            contentStream.endText();
            yPosition -= 20; // Move to the next line

            for (TimeEntry t : timeEntries) {
                if (t.getTaskId().equals(project.getId())) {
                    System.out.println("TimeEntry: " + t.getTaskId());
                    String timeEntry = t.getStartTime().toString();
                    System.out.println("TimeEntry: " + timeEntry);
                    String timeEntry2 = t.getEndTime().toString();
                    System.out.println("TimeEntry: " + timeEntry2);
                    String timeEntry3 = t.getTimePeriod().toString();

                    contentStream.beginText();
                    contentStream.showText("TimeEntry: " + timeEntries);
                    contentStream.endText();
                    yPosition -= 20;
                }

            }


            if (yPosition < 100) { // Seitenumbruch
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = 750;
            }
        }

        contentStream.close();

        // PDF-Dokument in ByteArrayOutputStream speichern
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream); // Speichern der PDF in den OutputStream
        document.close();

        // Rückgabe des PDF-Inhalts als Byte-Array
        return byteArrayOutputStream.toByteArray();
    }

    public ResponseEntity<?> createReport(Long userId, ReportSource reportSource) throws Exception {


        // Projekte abrufen
        List<Project> projects = projectRepository.findProjectByUserId(userId);
        if (projects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No projects found for user.", "projects", "NOT_FOUND"));
        }

        List<TimeEntry> timeEntries = new ArrayList<>();  // Initialize timeEntries list
        List<Long> taskIds = new ArrayList<>();  // Initialize taskIds list
        List<Task> tasks = new ArrayList<>();  // Initialize tasks list
        // Alle Tasks und TimeEntries sammeln
        for (Project project : projects) {
            Long projectId = project.getId();
             tasks = taskRepository.findByProjectId(projectId);

            // Füge Task-IDs zur taskIds-Liste hinzu
            List<Long> projectTaskIds = tasks.stream()
                    .map(Task::getTask_id)
                    .toList();
            taskIds.addAll(projectTaskIds);  // Accumulate task IDs from all projects
        }

        // Alle TimeEntries für die gesammelten taskIds holen
        if (!taskIds.isEmpty()) {
            timeEntries.addAll(timeEntryRepository.findByTaskIdIn(taskIds));  // Use findByTaskIdIn for a list of taskIds
        }
        // PDF erstellen
        byte[] pdfContent = createPdfReport(userId, projects, timeEntries, tasks);

        // Report speichern
        Report report = new Report(new UserId(userId), reportSource);
        report = reportRepository.save(report);

        // Event veröffentlichen
        ReportCreated event = new ReportCreated(this, report.getUserId(), report.getSource());
        eventPublisher.publishEvent(event);

        // Antwort zurückgeben
        if (pdfContent != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report-" + userId + ".pdf")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(pdfContent);
        } else {
            eventLogger.logError("Report could not be created.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponse("Report could not be created.", "report", "INVALID"));
        }
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }
}
