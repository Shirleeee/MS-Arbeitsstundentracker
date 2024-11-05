package de.vfh.workhourstracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name = "") //TODO: table name anpassen
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "") //TODO: column name anpassen
    private Long id;

//    @Column(name = "") //TODO: column name anpassen
    private Long userId;

//    @Column(name = "") //TODO: column name anpassen
    private String name;

//    @Column(name = "") //TODO: column name anpassen
    private String description;

//    @Column(name = "") //TODO: column name anpassen
    private String deadline;

}
