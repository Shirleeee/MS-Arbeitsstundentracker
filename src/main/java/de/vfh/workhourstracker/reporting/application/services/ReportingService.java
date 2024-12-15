package de.vfh.workhourstracker.reporting.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.reporting.domain.report.Report;
import de.vfh.workhourstracker.reporting.domain.report.ReportSource;
import de.vfh.workhourstracker.reporting.domain.report.events.ReportCreated;
import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;

import de.vfh.workhourstracker.reporting.infrastructure.repositories.ReportRepository;
import de.vfh.workhourstracker.shared.util.PdfCreator;
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
import java.util.List;

@Service
public class ReportingService {
    private final ApplicationEventPublisher eventPublisher;
    private final ReportRepository reportRepository;
    EventLogger eventLogger = new EventLogger();
private final ProjectRepository projectRepository;
    @Autowired
    public ReportingService(ApplicationEventPublisher eventPublisher, ReportRepository reportRepository, ProjectRepository projectRepository) {
        this.eventPublisher = eventPublisher;
        this.reportRepository = reportRepository;
        this.projectRepository = projectRepository;
    }
    public byte[] createPdfReport(Long userId, List<Project> projects) throws IOException {
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

        contentStream.beginText();
        contentStream.newLineAtOffset(50, yPosition);

        // Spaltenüberschrift
        contentStream.showText("Projekt ID | Projektname | Startdatum");
        contentStream.endText();

        yPosition -= 20; // Zeilenabstand

        // Projekte durchlaufen und in das PDF einfügen
        for (Project project : projects) {
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);

            // Hier fügst du die Projektdaten hinzu
            contentStream.showText(project.getId() + " | " + project.getName() + " | " + project.getDeadline());
            contentStream.endText();

            yPosition -= 20; // Zeilenabstand
            if (yPosition < 100) {  // Neue Seite, wenn der Platz knapp wird
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = 750;  // Startposition für die neue Seite
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

        List<Project> projects = projectRepository.findProjectByUserId(userId);

        byte[] pdfContent = createPdfReport(userId, projects);

        Report report = new Report(new UserId(userId), reportSource);
        report = reportRepository.save(report);

        ReportCreated event = new ReportCreated(this, report.getUserId(), report.getSource());
        eventPublisher.publishEvent(event);

        if (pdfContent != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report-" + userId + ".pdf")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(pdfContent);
        } else {

            eventLogger.logError("Report could not be created.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse("Report could not be created. ", "report", "INVALID"));
        }
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }
}
