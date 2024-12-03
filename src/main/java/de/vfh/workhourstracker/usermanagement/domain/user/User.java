package de.vfh.workhourstracker.usermanagement.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private UserName name;

    @Column(name = "mail_address")
    private MailAddress mailAddress;

    //TODO: brauchen wir ein Passwort?

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(UserName name, MailAddress mailAddress) {
        this.name = name;
        this.mailAddress = mailAddress;
    }
}
