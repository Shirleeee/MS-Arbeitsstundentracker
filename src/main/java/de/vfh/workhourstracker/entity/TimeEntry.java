package de.vfh.workhourstracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
//@Table(name = "timeentry") //TODO: table name anpassen
@Data
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id") //TODO: column name anpassen
    private Long id;

//    @Column(name = "taskId") //TODO: column name anpassen
    private Long taskId;

//    @Column(name = "startTime") //TODO: column name anpassen
    private LocalDateTime startTime;

//    @Column(name = "endTime") //TODO: column name anpassen
    private LocalDateTime endTime;

//    @Column(name = "duration") //TODO: column name anpassen
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
