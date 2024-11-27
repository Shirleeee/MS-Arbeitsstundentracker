package de.vfh.workhourstracker.usermanagement.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class UserName {
    private String name;

    public UserName(String name) {
        this.name = name;
    }
}
