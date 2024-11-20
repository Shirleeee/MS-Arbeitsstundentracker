package de.vfh.workhourstracker.projectmanagement.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    public Task() {
        // Java Persistence API JPA benötigt diesen Konstruktor, um Entitäten zu instanziieren???
    }

    public Task(Long projectId, String name, String description, LocalDateTime deadline) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }
}
