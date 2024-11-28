package de.vfh.workhourstracker.reporting.domain.report;

import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
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
    @Column(name = "id")
    private ReportId id;

    @Column(name = "user_id")
    private UserId userId;

    @Column(name = "link")
    private ReportSource source;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Report(UserId userId, ReportSource source) {
        this.userId = userId;
        this.source = source;
    }

}
