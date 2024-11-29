package de.vfh.workhourstracker.projectmanagement.domain.project;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ProjectName {

    private String projectName;

    public ProjectName(String projectName) {
        this.projectName = projectName;
    }
}
