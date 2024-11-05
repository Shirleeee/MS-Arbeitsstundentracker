package de.vfh.workhourstracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name = "projects") //TODO: table name anpassen
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id") //TODO: column name anpassen
    private Long id;

//    @Column(name = "userId") //TODO: column name anpassen
    private Long userId;

//    @Column(name = "name") //TODO: column name anpassen
    private String name;

//    @Column(name = "description") //TODO: column name anpassen
    private String description;

//    @Column(name = "deadline") //TODO: column name anpassen
    private String deadline;

}
