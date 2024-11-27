package de.vfh.workhourstracker.timemanagement.domain.timeentry;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class TimeEntryId {
    private Long timeEntryId;

    public TimeEntryId(Long timeEntryId) {
        this.timeEntryId = timeEntryId;
    }
}
