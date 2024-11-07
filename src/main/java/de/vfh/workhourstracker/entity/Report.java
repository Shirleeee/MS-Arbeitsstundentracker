package de.vfh.workhourstracker.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "link")
    private String link;

    public Report() {

    }
    public Report(Long userId, LocalDateTime date, String link) {
        this.userId = userId;
        this.date = date;
        this.link = link;
    }

}
