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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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
import java.util.concurrent.atomic.AtomicReference;
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
        PDDocument document = new PDDocument();
        List<PDPageContentStream> contentStreams = new ArrayList<>();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStreams.add(contentStream);

        AtomicReference<Float> yPosition = new AtomicReference<>(750f);
        float margin = 50;
        float lineHeight = 20;

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
        contentStream.newLineAtOffset(margin, yPosition.get());
        contentStream.showText("Report für Benutzer: " + userId);
        contentStream.endText();
        yPosition.set(yPosition.get() - 40);
        List<TimeEntry> taskTimeEntries = new ArrayList<>();
        List<Task> projectTasks = new ArrayList<>();


        for (Project project : projects) {
            yPosition.set(writeTextWithCheck(document, contentStreams, "Projekt Titel: " + project.getId() + "-" + sanitize(project.getName().getProjectName()), margin, yPosition.get()));
            yPosition.set(writeTextWithCheck(document, contentStreams, "Projekt Beschreibung: " + sanitize(project.getDescription().getProjectDescription()), margin, yPosition.get()));
            yPosition.set(writeTextWithCheck(document, contentStreams, "Projekt Deadline: " + sanitize(project.getDeadline().getDeadline()), margin, yPosition.get()));
            yPosition.set(yPosition.get() - 10);

            projectTasks = tasks.stream()
                    .filter(task -> task.getProjectId() != null && task.getProjectId().toString().equals(project.getId().toString()))
                    .toList();
            yPosition.set(writeTextWithCheck(document, contentStreams, "Gefundene Tasks für Projekt " + project.getId() + ": " + projectTasks.size(), margin, yPosition.get()));

            for (Task task : projectTasks) {
                yPosition.set(writeTextWithCheck(document, contentStreams, "Task Titel: " + task.getTask_id() + "-" + sanitize(task.getName().getTaskName()), margin + 20, yPosition.get()));
                yPosition.set(writeTextWithCheck(document, contentStreams, "Task Beschreibung: " + sanitize(task.getDescription().getTaskDescription()), margin + 20, yPosition.get()));
                yPosition.set(writeTextWithCheck(document, contentStreams, "Task Deadline: " + sanitize(task.getDeadline().getDeadline()), margin + 20, yPosition.get()));
                yPosition.set(yPosition.get() - 10);

           taskTimeEntries = timeEntries.stream()
                        .filter(timeEntry -> timeEntry.getTaskId().equals(task.getTask_id()))
                        .toList();

                for (TimeEntry timeEntry : taskTimeEntries) {
                    yPosition.set(writeTextWithCheck(
                            document,
                            contentStreams,
                            String.format("    %s   Start: %s | End: %s ", timeEntry.getId(),
                                    sanitize(timeEntry.getStartTime().getStartTime()), sanitize(timeEntry.getEndTime().getEndTime())),
                            margin + 40,
                            yPosition.get()
                    ));
                    yPosition.set(writeTextWithCheck(
                            document,
                            contentStreams,
                            String.format("    Period: %s",
                                    sanitize(timeEntry.getTimePeriod().getTimePeriod())),
                            margin + 40,
                            yPosition.get()
                    ));
                    yPosition.set(yPosition.get() - 10);
                }
                yPosition.set(yPosition.get() - 10);
            }
            yPosition.set(yPosition.get() - 20);
        }

        for (PDPageContentStream stream : contentStreams) {
            stream.close();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }




    // Verbesserte Methode für Zeilenumbrüche und Seitenwechsel
    private float writeTextWithCheck(PDDocument document, List<PDPageContentStream> contentStreams, String text, float x, float y) throws IOException {
        PDPageContentStream contentStream = contentStreams.getLast(); // Hole den letzten Stream
        if (y < 100) { // Wenig Platz auf Seite
            contentStream.close(); // Alten Stream schließen
            PDPage newPage = new PDPage(PDRectangle.A4);// Neue Seite erstellen
            document.addPage(newPage);
            contentStream = new PDPageContentStream(document, newPage); // Neuer Stream
            contentStreams.add(contentStream); // Neuen Stream zur Liste hinzufügen
            y = 750; // Neustart auf neuer Seite
        }
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        return y - 20; // Neue Y-Position zurückgeben
    }

    // Hilfsmethode zur Sanitierung von Text
    private String sanitize(Object input) {
        return input != null ? input.toString().replace("\n", " ") : "N/A";
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
