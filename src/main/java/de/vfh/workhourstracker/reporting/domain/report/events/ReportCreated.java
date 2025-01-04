package de.vfh.workhourstracker.reporting.domain.report.events;

import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ReportCreated extends ApplicationEvent {
    private final UserId userId;
    private final Long projId;
    private final LocalDateTime occurredAt;

    public ReportCreated(Object source, UserId userId, Long projId) {
        super(source);
        this.userId = userId;
        this.projId = projId;
        this.occurredAt = LocalDateTime.now();
    }
}
