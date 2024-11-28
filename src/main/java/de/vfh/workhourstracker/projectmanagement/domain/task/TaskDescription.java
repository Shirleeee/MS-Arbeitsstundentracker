package de.vfh.workhourstracker.projectmanagement.domain.task;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class TaskDescription {
    private String taskDescription;

    public TaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
