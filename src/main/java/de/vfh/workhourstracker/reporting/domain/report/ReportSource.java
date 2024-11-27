package de.vfh.workhourstracker.reporting.domain.report;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ReportSource {
    //TODO: überlegen, wie wir den Report speichern wollen
    private String reportSource;

    public ReportSource(String reportSource) {
        this.reportSource = reportSource;
    }
}
