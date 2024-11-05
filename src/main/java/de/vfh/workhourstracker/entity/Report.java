package de.vfh.workhourstracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name = "reports") //TODO: table name anpassen
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //    @Column(name = "id") //TODO: column name anpassen
    private Long id;

    //    @Column(name = "userId") //TODO: column name anpassen
    private Long userId;

    //    @Column(name = "date") //TODO: column name anpassen
    private String date;

    //    @Column(name = "link") //TODO: column name anpassen
    private String link;

}
