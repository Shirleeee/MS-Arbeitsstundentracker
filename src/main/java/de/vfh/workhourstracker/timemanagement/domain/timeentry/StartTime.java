package de.vfh.workhourstracker.timemanagement.domain.timeentry;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@Getter
public class StartTime {
    private LocalDateTime endTime;

    public StartTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
