package de.vfh.workhourstracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name = "users") //TODO: table name anpassen
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id") //TODO: column name anpassen
    private Long id;

//    @Column(name = "name") //TODO: column name anpassen
    private String name;

//    @Column(name = "email") //TODO: column name anpassen
    private String mailAddress;

}
