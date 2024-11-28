package de.vfh.workhourstracker.timemanagement.domain.timeentry.events;

import de.vfh.workhourstracker.projectmanagement.domain.task.TaskId;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.StartTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntryId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class TimeTrackingStarted extends ApplicationEvent {
    private final TimeEntryId timeEntryId;
    private final TaskId taskId;
    private final StartTime startTime;
    private final LocalDateTime occurredAt;

    public TimeTrackingStarted(Object source, TimeEntryId timeEntryId, TaskId taskId, StartTime startTime) {
        super(source);
        this.timeEntryId = timeEntryId;
        this.taskId = taskId;
        this.startTime = startTime;
        this.occurredAt = LocalDateTime.now();
    }
}
