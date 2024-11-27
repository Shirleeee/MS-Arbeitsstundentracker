package de.vfh.workhourstracker.projectmanagement.domain.project;

import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private ProjectId id;

    @Column(name = "user_id")
    private UserId userId;

    //    @Embedded
//    @AttributeOverride(name = "projectName", column = @Column(name = "name"))
    @Column(name = "name")
    private ProjectName name;

    @Column(name = "description")
    private ProjectDescription description;

    @Column(name = "deadline")
    private Deadline deadline;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Project(UserId userId, ProjectName name, ProjectDescription description, Deadline deadline) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }
}
