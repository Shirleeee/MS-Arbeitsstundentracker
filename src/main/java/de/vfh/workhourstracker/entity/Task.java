package de.vfh.workhourstracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
//@Table(name = "tasks") //TODO: table name anpassen
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id") //TODO: column name anpassen
    private Long id;

//    @Column(name = "projectId") //TODO: column name anpassen
    private Long projectId;

//    @Column(name = "name") //TODO: column name anpassen
    private String name;

//    @Column(name = "description") //TODO: column name anpassen
    private String description;

//    @Column(name = "deadline") //TODO: column name anpassen
    private String deadline;

}
