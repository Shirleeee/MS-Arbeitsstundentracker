package de.vfh.workhourstracker.reporting.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportGeneratorService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public byte[] createPdfReport(Long userId, List<Project> projects, List<TimeEntry> timeEntries, List<Task> tasks) throws IOException {
        try (PDDocument document = new PDDocument()) { // PDDocument wird automatisch geschlossen
            PDPage page = new PDPage();
            document.addPage(page);

            float margin = 50;
            AtomicReference<Float> yStartPosition = new AtomicReference<>(page.getMediaBox().getHeight() - margin);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) { // ContentStream im try-with-resources
                // Schreibe Benutzerinformationen
                yStartPosition.set(writeTextWithCheck(document, List.of(contentStream), "Bericht für Benutzer: " + userId, margin, yStartPosition.get()));
                yStartPosition.set(yStartPosition.get() - 20);

                // Verarbeite Projekte und ihre Aufgaben
                for (Project project : projects) {
                    this.processProject(document, List.of(contentStream), project, tasks, timeEntries, margin, yStartPosition);
                }

                // Dokument speichern und zurückgeben
                return saveDocument(document);
            }
        }
    }


    private byte[] saveDocument(PDDocument document) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    // Hilfsmethode zur Bereinigung von Text
    public String sanitize(Object input) {
        return  input != null ? input.toString().replaceAll("[\r\n]+", " ") : "N/A";

    }


    private String parseDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = (duration.toMinutes() % 60);
        long seconds = duration.getSeconds() % 60;

        return "Dauer: " + hours + " Stunden, " + minutes + " Minuten und " + seconds + " Sekunden.";
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

    private List<Task> filterTasksByProject(List<Task> tasks, Long projectId) {
        return tasks.stream()
                .filter(task -> task.getProjectId() != null && task.getProjectId().equals(projectId))
                .toList();
    }

    private List<TimeEntry> filterTimeEntriesByTask(List<TimeEntry> timeEntries, Long taskId) {
        return timeEntries.stream()
                .filter(timeEntry -> timeEntry.getTaskId().equals(taskId))
                .toList();
    }


    private void processProject(PDDocument document, List<PDPageContentStream> contentStreams, Project project,
                                List<Task> tasks, List<TimeEntry> timeEntries, float margin, AtomicReference<Float> yPosition) throws IOException {

        writeProjectDetails(document, contentStreams, project, margin, yPosition);

        List<Task> projectTasks = filterTasksByProject(tasks, project.getId());
        writeTaskSummary(document, contentStreams, project.getId(), projectTasks.size(), margin, yPosition);

        for (Task task : projectTasks) {
            processTask(document, contentStreams, task, timeEntries, margin, yPosition);
        }

        yPosition.set(yPosition.get() - 20);
    }

    private void processTask(PDDocument document, List<PDPageContentStream> contentStreams, Task task,
                             List<TimeEntry> timeEntries, float margin, AtomicReference<Float> yPosition) throws IOException {
        writeTaskDetails(document, contentStreams, task, margin + 20, yPosition);

        List<TimeEntry> taskTimeEntries = filterTimeEntriesByTask(timeEntries, task.getTask_id());
        for (TimeEntry timeEntry : taskTimeEntries) {
            writeTimeEntryDetails(document, contentStreams, timeEntry, margin + 40, yPosition);
        }

        yPosition.set(yPosition.get() - 10);
    }

    private void writeProjectDetails(PDDocument document, List<PDPageContentStream> contentStreams, Project project,
                                     float margin, AtomicReference<Float> yPosition) throws IOException {
        yPosition.set(writeTextWithCheck(document, contentStreams, "Projekt Titel: " + project.getId() + "-" +
                sanitize(project.getName().getProjectName()), margin, yPosition.get()));
        yPosition.set(writeTextWithCheck(document, contentStreams, "Projekt Beschreibung: " +
                sanitize(project.getDescription().getProjectDescription()), margin, yPosition.get()));
        yPosition.set(writeTextWithCheck(document, contentStreams, "Projekt Deadline: " +
                sanitize(project.getDeadline().getDeadline().format(formatter)), margin, yPosition.get()));
        yPosition.set(yPosition.get() - 10);


    }

    private void writeTaskSummary(PDDocument document, List<PDPageContentStream> contentStreams, Long projectId,
                                  int taskCount, float margin, AtomicReference<Float> yPosition) throws IOException {
        yPosition.set(writeTextWithCheck(document, contentStreams,
                "Gefundene Tasks für Projekt " + projectId + ": " + taskCount, margin, yPosition.get()));
    }

    private void writeTaskDetails(PDDocument document, List<PDPageContentStream> contentStreams, Task task,
                                  float margin, AtomicReference<Float> yPosition) throws IOException {
        yPosition.set(writeTextWithCheck(document, contentStreams, "Task Titel: " + task.getTask_id() + "-" +
                sanitize(task.getName().getTaskName()), margin, yPosition.get()));
        yPosition.set(writeTextWithCheck(document, contentStreams, "Task Beschreibung: " +
                sanitize(task.getDescription().getTaskDescription()), margin, yPosition.get()));
        yPosition.set(writeTextWithCheck(document, contentStreams, "Task Deadline: " +
                sanitize(task.getDeadline().getDeadline().format(formatter)), margin, yPosition.get()));
        yPosition.set(yPosition.get() - 10);
    }

    private void writeTimeEntryDetails(PDDocument document, List<PDPageContentStream> contentStreams, TimeEntry timeEntry,
                                       float margin, AtomicReference<Float> yPosition) throws IOException {
        String endTime = timeEntry.getEndTime() != null ? sanitize(timeEntry.getEndTime().getEndTime().format(formatter)) : "N/A";
        yPosition.set(writeTextWithCheck(document, contentStreams,
                String.format("    %s   Start: %s | End: %s", timeEntry.getId(),
                        sanitize(timeEntry.getStartTime().getStartTime().format(formatter)), endTime), margin, yPosition.get()));
        yPosition.set(writeTextWithCheck(document, contentStreams,
                String.format("    Period: %s", sanitize(parseDuration(timeEntry.getTimePeriod().getTimePeriod()))), margin, yPosition.get()));
        yPosition.set(yPosition.get() - 10);
    }
}
