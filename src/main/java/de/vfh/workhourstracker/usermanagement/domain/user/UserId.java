package de.vfh.workhourstracker.usermanagement.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class UserId {
    private Long id;

    public UserId(Long id) {
        this.id = id;
    }
}
