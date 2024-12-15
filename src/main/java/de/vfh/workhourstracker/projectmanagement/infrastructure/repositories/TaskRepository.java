package de.vfh.workhourstracker.projectmanagement.infrastructure.repositories;

import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository {
    Task save(Task task);

    Optional<Task> findById(Long id);
    List<Task> findAll();
    List<Task> findByProjectId(Long projectId);
    void deleteById(Long id);
}
