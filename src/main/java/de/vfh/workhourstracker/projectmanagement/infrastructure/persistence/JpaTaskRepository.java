package de.vfh.workhourstracker.projectmanagement.infrastructure.persistence;

import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository extends JpaRepository<Task, Long>, TaskRepository {
}
