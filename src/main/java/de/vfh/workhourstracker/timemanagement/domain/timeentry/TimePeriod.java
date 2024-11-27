package de.vfh.workhourstracker.timemanagement.domain.timeentry;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Embeddable
@Getter
@NoArgsConstructor
public class TimePeriod {
    private Duration timePeriod;

    public TimePeriod(Duration timePeriod) {
        this.timePeriod = timePeriod;
    }
}
