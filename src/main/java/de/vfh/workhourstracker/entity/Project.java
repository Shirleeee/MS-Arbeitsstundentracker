package de.vfh.workhourstracker.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime deadline;

    public Project() {
        // JPA benötigt diesen Konstruktor, um Entitäten zu instanziieren???
    }

    public Project(Long userId, String name,String description, LocalDateTime deadline) {
        this.userId = userId;
        this.name =  name;
        this.description = description;
        this.deadline = deadline;
    }


}
