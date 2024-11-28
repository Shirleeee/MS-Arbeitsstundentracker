package de.vfh.workhourstracker.projectmanagement.domain.task;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class TaskName {
    private String taskName;

    public TaskName(String taskName) {
        this.taskName = taskName;
    }
}
