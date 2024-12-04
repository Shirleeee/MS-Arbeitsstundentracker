package de.vfh.workhourstracker.projectmanagement.domain.project;

import de.vfh.workhourstracker.projectmanagement.domain.valueobjects.Deadline;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
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
    @Basic(optional = false)
    @Column(name = "proj_id")
    @NotNull
    private Long id;

    @Column(name = "id")
    private Long userId;

    //    @Embedded
//    @AttributeOverride(name = "value", column = @Column(name = "project_name"))
    @Column(name = "project_name")
    private ProjectName name;

    //    @Embedded
//    @AttributeOverride(name = "value", column = @Column(name = "project_description"))
    @Column(name = "project_description")

    private ProjectDescription description;

    //    @Embedded
//    @AttributeOverride(name = "value", column = @Column(name = "deadline"))
    @Column(name = "deadline")
    private Deadline deadline;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Project(Long userId, ProjectName name, ProjectDescription description, Deadline deadline) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }
}
