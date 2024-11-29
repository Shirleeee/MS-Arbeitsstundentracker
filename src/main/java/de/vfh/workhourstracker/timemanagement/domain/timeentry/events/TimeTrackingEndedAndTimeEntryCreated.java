package de.vfh.workhourstracker.timemanagement.domain.timeentry.events;

import de.vfh.workhourstracker.projectmanagement.domain.task.TaskId;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.EndTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.StartTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntryId;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimePeriod;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class TimeTrackingEndedAndTimeEntryCreated extends ApplicationEvent {
    private final Long id;
    private final TaskId taskId;
    private final StartTime startTime;
    private final EndTime endTime;
    private final TimePeriod timePeriod;
    private final LocalDateTime occurredAt;

    public TimeTrackingEndedAndTimeEntryCreated(Object source, Long id, TaskId taskId, StartTime startTime, EndTime endTime, TimePeriod timePeriod) {
        super(source);
        this.id = id;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timePeriod = timePeriod;
        this.occurredAt = LocalDateTime.now();
    }
}
