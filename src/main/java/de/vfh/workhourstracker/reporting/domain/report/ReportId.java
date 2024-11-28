package de.vfh.workhourstracker.reporting.domain.report;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ReportId {
    private Long reportId;

    public ReportId(Long reportId) {
        this.reportId = reportId;
    }
}
