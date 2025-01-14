package de.vfh.workhourstracker.reporting.domain.report;

import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectId;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(insertable = false, updatable = false)
    private Long id;


    @Column(name = "user_id")
    private UserId userId;

    @Column(name = "proj_id")
    private ProjectId projId;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Report(UserId userId, ProjectId projId) {
        this.userId = userId;
        this.projId = projId;
    }

}
