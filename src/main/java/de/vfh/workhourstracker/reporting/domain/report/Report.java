package de.vfh.workhourstracker.reporting.domain.report;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Column(insertable=false, updatable=false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "proj_id")
    private Long projId;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Report(Long userId, Long projId) {
        this.userId = userId;
        this.projId = projId;
    }

}
