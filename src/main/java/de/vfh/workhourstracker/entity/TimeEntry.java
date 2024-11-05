package de.vfh.workhourstracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

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
    private String startTime;

//    @Column(name = "endTime") //TODO: column name anpassen
    private String endTime;

//    @Column(name = "duration") //TODO: column name anpassen
    private Long duration;

}
