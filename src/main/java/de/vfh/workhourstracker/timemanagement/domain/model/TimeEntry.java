package de.vfh.workhourstracker.timemanagement.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_entry")
@Data
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private Duration duration;


    public TimeEntry() {

    }
    public TimeEntry(Long taskId, LocalDateTime startTime, LocalDateTime endTime, Duration duration) {
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }
}
