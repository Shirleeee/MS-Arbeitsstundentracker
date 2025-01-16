package de.vfh.workhourstracker.reporting.domain.report.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ReportCreated extends ApplicationEvent {
    private final Long userId;
    private final Long projId;
    private final LocalDateTime occurredAt;

    public ReportCreated(Object source, Long userId, Long projId) {
        super(source);
        this.userId = userId;
        this.projId = projId;
        this.occurredAt = LocalDateTime.now();
    }
}
