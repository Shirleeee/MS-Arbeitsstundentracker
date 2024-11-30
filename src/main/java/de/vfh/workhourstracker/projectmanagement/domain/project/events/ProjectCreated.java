package de.vfh.workhourstracker.projectmanagement.domain.project.events;

import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectDescription;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectId;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectName;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ProjectCreated extends ApplicationEvent {

    private final UserId userId;
    private final ProjectName projectName;
    private final ProjectDescription projectDescription;
    private final Deadline deadline;
    private final LocalDateTime created_at;

    public ProjectCreated(Object source, UserId userId, ProjectName projectName, ProjectDescription projectDescription, Deadline deadline) {
        super(source);

        this.userId = userId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.deadline = deadline;
        this.created_at = LocalDateTime.now();
    }
}
