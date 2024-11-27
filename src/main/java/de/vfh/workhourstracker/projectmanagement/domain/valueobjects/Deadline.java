package de.vfh.workhourstracker.projectmanagement.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor
public class Deadline {
    private LocalDateTime deadline;

    public Deadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
