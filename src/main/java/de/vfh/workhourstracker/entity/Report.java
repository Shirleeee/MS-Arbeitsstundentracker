package de.vfh.workhourstracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name = "") //TODO: table name anpassen
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //    @Column(name = "") //TODO: column name anpassen
    private Long id;

    //    @Column(name = "") //TODO: column name anpassen
    private Long userId;

    //    @Column(name = "") //TODO: column name anpassen
    private String date;

    //    @Column(name = "") //TODO: column name anpassen
    private String link;

}
