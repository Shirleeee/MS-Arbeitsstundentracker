package de.vfh.workhourstracker.projectmanagement.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "project")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    public Project() {
        // JPA benötigt diesen Konstruktor, um Entitäten zu instanziieren???
    }

    public Project( String name,String description, LocalDateTime deadline) {

        this.name =  name;
        this.description = description;
        this.deadline = deadline;
    }


}
