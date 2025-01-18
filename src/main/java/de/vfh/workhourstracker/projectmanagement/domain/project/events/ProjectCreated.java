package de.vfh.workhourstracker.projectmanagement.domain.project.events;

import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ProjectCreated extends ApplicationEvent {

    private final Long userId;
    private final ProjectName projectName;
    private final ProjectDescription projectDescription;
    private final Deadline deadline;
    private final LocalDateTime createdAt;

    public ProjectCreated(Object source, Long userId, ProjectName projectName, ProjectDescription projectDescription, Deadline deadline) {
        super(source);
        this.userId = userId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
    }
}
