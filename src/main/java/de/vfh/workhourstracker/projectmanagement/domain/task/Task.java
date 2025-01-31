package de.vfh.workhourstracker.projectmanagement.domain.task;

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
    @Column(name = "task_id", insertable = false, updatable = false)
    private Long task_id;

    @Column(name = "project_id")
    private Long projectId;

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

    public Task(Long projectId, TaskName name, TaskDescription description, Deadline deadline) {

        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

}
