package de.vfh.workhourstracker.reporting.domain.report;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor
public class CreationDate {
    private LocalDateTime creationDate;

    public CreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
