package de.vfh.workhourstracker.projectmanagement.domain.task.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskDeleted extends ApplicationEvent {

    private final Long taskId;

    public TaskDeleted(Object source, Long taskId) {
        super(source);
        this.taskId = taskId;
    }
}
