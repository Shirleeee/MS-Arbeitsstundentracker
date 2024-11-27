package de.vfh.workhourstracker.projectmanagement.domain.task;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class TaskId {
    private Long taskId;

    public TaskId(Long taskId) {
        this.taskId = taskId;
    }

}
