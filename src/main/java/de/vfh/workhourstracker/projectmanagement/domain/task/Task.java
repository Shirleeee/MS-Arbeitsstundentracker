package de.vfh.workhourstracker.projectmanagement.domain.task;

import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectId;
import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "project_id"))
    private ProjectId projectId;

    @Column(name = "task_name")
    private TaskName name;

    @Column(name = "task_description")
    private TaskDescription description;

    @Column(name = "deadline")
    private Deadline deadline;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Task(ProjectId projectId, TaskName name, TaskDescription description, Deadline deadline) {

        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }
}
