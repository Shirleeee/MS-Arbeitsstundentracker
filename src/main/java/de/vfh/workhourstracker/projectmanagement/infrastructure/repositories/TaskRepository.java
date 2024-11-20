package de.vfh.workhourstracker.projectmanagement.infrastructure.repositories;

import de.vfh.workhourstracker.projectmanagement.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
