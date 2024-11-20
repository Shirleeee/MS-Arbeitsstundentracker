package de.vfh.workhourstracker.usermanagement.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mail_address")
    private String mailAddress;


    public User() {

    }
    public User( String name, String mailAddress) {
        this.name = name;
        this.mailAddress = mailAddress;

    }
}
