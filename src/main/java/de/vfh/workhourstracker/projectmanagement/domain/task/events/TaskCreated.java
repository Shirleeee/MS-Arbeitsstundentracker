package de.vfh.workhourstracker.projectmanagement.domain.task.events;

import de.vfh.workhourstracker.projectmanagement.domain.task.TaskDescription;
import de.vfh.workhourstracker.projectmanagement.domain.task.TaskName;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class TaskCreated extends ApplicationEvent {
    private final Long taskId;
    private final Long projectId;
    private final TaskName taskName;
    private final TaskDescription taskDescription;
    private final Deadline deadline;
    private final LocalDateTime occurredAt;

    public TaskCreated(Object source, Long taskId, Long projectId, TaskName taskName, TaskDescription taskDescription, Deadline deadline) {
        super(source);
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.occurredAt = LocalDateTime.now();
    }
}
