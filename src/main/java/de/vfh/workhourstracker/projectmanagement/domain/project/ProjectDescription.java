package de.vfh.workhourstracker.projectmanagement.domain.project;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ProjectDescription {
    private String projectName;

    public ProjectDescription(String projectName) {
        this.projectName = projectName;
    }
}
