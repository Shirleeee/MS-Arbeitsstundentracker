package de.vfh.workhourstracker.reporting.domain.model;

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
    public Report( LocalDateTime date, String link) {
        this.date = date;
        this.link = link;
    }

}