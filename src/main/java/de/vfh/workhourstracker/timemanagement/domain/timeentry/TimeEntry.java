package de.vfh.workhourstracker.timemanagement.domain.timeentry;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_entry")
@Data
@NoArgsConstructor
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "start_time")
    private StartTime startTime;

    @Column(name = "end_time")
    private EndTime endTime;

    @Column(name = "time_period")
    private TimePeriod timePeriod;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TimeEntry(Long taskId, StartTime startTime, EndTime endTime, TimePeriod timePeriod) {
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timePeriod = timePeriod;
    }
}
