package de.vfh.workhourstracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
//@Table(name = "") //TODO: table name anpassen
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "") //TODO: column name anpassen
    private Long id;

//    @Column(name = "") //TODO: column name anpassen
    private Long projectId;

//    @Column(name = "") //TODO: column name anpassen
    private String name;

//    @Column(name = "") //TODO: column name anpassen
    private String description;

//    @Column(name = "") //TODO: column name anpassen
    private String deadline;

}
