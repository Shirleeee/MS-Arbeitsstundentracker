package de.vfh.workhourstracker.timemanagement.domain.timeentry;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@Getter
public class EndTime {
    private LocalDateTime endTime;

    public EndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
