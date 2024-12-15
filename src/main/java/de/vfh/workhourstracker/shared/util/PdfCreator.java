package de.vfh.workhourstracker.shared.util;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.util.List;


public class PdfCreator {

    public void createPdfReport(String pdfPath, List<Project> projects) throws Exception {

        // Neues PDF-Dokument erstellen
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Inhalt streamen
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Überschrift
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 20);
        contentStream.endText();

        // Startposition für die Tabelle
        float yPosition = 720;
        // Spaltenüberschriften für die Tabelle
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, yPosition);
        contentStream.showText("ID");
        contentStream.newLineAtOffset(100, 0); // Abstand zur zweiten Spalte
        contentStream.showText("Erstellungsdatum");
        contentStream.newLineAtOffset(150, 0); // Abstand zur dritten Spalte
        contentStream.showText("Titel");
        contentStream.newLineAtOffset(200, 0); // Abstand zur vierten Spalte
        contentStream.showText("Beschreibung");
        contentStream.endText();

        yPosition -= 20; // Zeilenabstand nach den Spaltenüberschriften

        // Daten schreiben
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
        for (Project project : projects) {
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText(String.valueOf(project.getId()));
            contentStream.newLineAtOffset(100, 0); // Abstand zur zweiten Spalte
            contentStream.showText(project.getDeadline().toString());
            contentStream.newLineAtOffset(150, 0); // Abstand zur dritten Spalte
            contentStream.showText(String.valueOf(project.getName()));
            contentStream.newLineAtOffset(200, 0); // Abstand zur vierten Spalte
            contentStream.showText(String.valueOf(project.getDescription()));
            contentStream.endText();

            yPosition -= 20; // Zeilenabstand
        }


// PDF-Stream schließen
        contentStream.close();

        // Dokument speichern
        document.save(pdfPath);
        document.close();

        System.out.println("PDF erfolgreich erstellt: " + pdfPath);
    }
}


