package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskDescription;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskName;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.reporting.application.services.ReportGeneratorService;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.EndTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.StartTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimePeriod;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
 class ReportGeneratorServiceTest {

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    //region testSanitize
    @Test
    void testSanitize() {
        // Test with null input
        String result = reportGeneratorService.sanitize(null);
        assertEquals("N/A", result);

        // Test with normal string
        result = reportGeneratorService.sanitize("Hello World");
        assertEquals("Hello World", result);

        // Test with string containing newline
        result = reportGeneratorService.sanitize("Hello\nWorld");
        assertEquals("Hello World", result);

        // Test with string containing carriage return
        result = reportGeneratorService.sanitize("Hello\rWorld");
        assertEquals("Hello World", result);

        // Test with string containing both newline and carriage return
        result = reportGeneratorService.sanitize("Hello\r\nWorld");
        assertEquals("Hello World", result);
    }
    //endregion

    //region testCreatePdfReport
    @Test
     void testCreatePdfReport() throws IOException {
        Long userId = 1L;
        List<Project> projects = IntStream.range(1, 6)
                .mapToObj(i -> new Project(
                        userId,
                        new ProjectName("ProjectTitle " + i),
                        new ProjectDescription("ProjectDescription " + i),
                        new Deadline(LocalDateTime.now().plusDays(i))
                ))
                .collect(Collectors.toList());

        List<Task> tasks = IntStream.range(1, 6)
                .mapToObj(i -> new Task(
                        (long) i,
                        new TaskName("TaskTitle " + i),
                        new TaskDescription("TaskDescription " + i),
                        new Deadline(LocalDateTime.now().plusDays(i))
                ))
                .collect(Collectors.toList());

        List<TimeEntry> timeEntries = IntStream.range(1, 6)
                .mapToObj(i -> new TimeEntry(
                        (long) i,
                        new StartTime(LocalDateTime.now().plusDays(i)),
                        new EndTime(LocalDateTime.now().plusDays(i)),
                        new TimePeriod(Duration.ofHours(1))
                ))
                .collect(Collectors.toList());

        byte[] pdfContent = reportGeneratorService.createPdfReport(userId, projects, timeEntries, tasks);

        assertNotNull(pdfContent);

        // Konvertiere das PDF-Byte-Array in ein PDDocument
        PDDocument document = Loader.loadPDF(pdfContent);
        // Extrahiere den Text aus dem PDF
        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(document);
        System.out.println(pdfText);
        // Überprüfe, ob der Text bestimmte Inhalte enthält
        assertTrue(pdfText.contains("ProjectTitle 1"));
    }

    @Test
    void testCreatePdfReportWithOnlyProjects() throws IOException {
        Long userId = 1L;
        List<Project> projects = IntStream.range(1, 3)
                .mapToObj(i -> new Project(userId, new ProjectName("Project " + i), new ProjectDescription("Desc " + i), new Deadline(LocalDateTime.now())))
                .collect(Collectors.toList());
        List<Task> tasks = Collections.emptyList();
        List<TimeEntry> timeEntries = Collections.emptyList();

        byte[] pdfContent = reportGeneratorService.createPdfReport(userId, projects, timeEntries, tasks);
        assertNotNull(pdfContent);

        PDDocument document = Loader.loadPDF(pdfContent);
        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(document);

        assertTrue(pdfText.contains("Project 1"));
    }

    @Test
    void testCreatePdfReportWithInvalidUserId() {
        Long userId = -1L;
        List<Project> projects = Collections.emptyList();
        List<Task> tasks = Collections.emptyList();
        List<TimeEntry> timeEntries = Collections.emptyList();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportGeneratorService.createPdfReport(userId, projects, timeEntries, tasks);
        });

        assertEquals("Invalid user ID", exception.getMessage());
    }

    @Test
    void testCreatePdfReportWithEmptyLists() throws IOException {
        Long userId = 1L;
        List<Project> projects = Collections.emptyList();
        List<Task> tasks = Collections.emptyList();
        List<TimeEntry> timeEntries = Collections.emptyList();

        byte[] pdfContent = reportGeneratorService.createPdfReport(userId, projects, timeEntries, tasks);
        assertNotNull(pdfContent);
    }

    @Test
    void testCreatePdfReportWithNullInputs() {
        assertThrows(IllegalArgumentException.class, () -> {
            reportGeneratorService.createPdfReport(null, null, null, null);
        });
    }

    @Test
    void testPdfPageCount() throws IOException {
        Long userId = 1L;
        List<Project> projects = IntStream.range(1, 6)
                .mapToObj(i -> new Project(userId, new ProjectName("Project " + i), new ProjectDescription("Description " + i), new Deadline(LocalDateTime.now())))
                .collect(Collectors.toList());
        List<Task> tasks = Collections.emptyList();
        List<TimeEntry> timeEntries = Collections.emptyList();

        byte[] pdfContent = reportGeneratorService.createPdfReport(userId, projects, timeEntries, tasks);

        PDDocument document = Loader.loadPDF(pdfContent);
        assertEquals(1, document.getNumberOfPages()); // Prüfe Seitenanzahl
    }
}
