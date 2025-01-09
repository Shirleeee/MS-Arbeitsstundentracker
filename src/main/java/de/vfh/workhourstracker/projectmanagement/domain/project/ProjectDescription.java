package de.vfh.workhourstracker.projectmanagement.domain.project;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ProjectDescription {

    private String projectDescription;

    public ProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
}
