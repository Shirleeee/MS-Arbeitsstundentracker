package de.vfh.workhourstracker.projectmanagement.domain.project.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectDeleted extends ApplicationEvent {
    private final Long projectId;

    public ProjectDeleted(Object source, Long projectId) {
        super(source);
        this.projectId = projectId;
    }
}
