package de.vfh.workhourstracker.reporting.domain.report.events;

import de.vfh.workhourstracker.reporting.domain.report.ReportId;
import de.vfh.workhourstracker.reporting.domain.report.ReportSource;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ReportCreated extends ApplicationEvent {
    private final ReportId reportId;
    private final UserId userId;
    private final ReportSource reportSource;
    private final LocalDateTime occurredAt;

    public ReportCreated(Object source, ReportId reportId, UserId userId, ReportSource reportSource) {
        super(source);
        this.reportId = reportId;
        this.userId = userId;
        this.reportSource = reportSource;
        this.occurredAt = LocalDateTime.now();
    }
}
