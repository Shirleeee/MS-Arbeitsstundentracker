package de.vfh.workhourstracker.projectmanagement.domain.project;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ProjectId {
    private Long value;

    public ProjectId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is null");
        }

        this.value = value;
    }
}
