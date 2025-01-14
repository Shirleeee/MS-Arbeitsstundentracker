package de.vfh.workhourstracker.timemanagement.infrastructure.repositories;

import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository  {
    TimeEntry save(TimeEntry timeEntry);

    Optional<TimeEntry> findById(Long id);
    List<TimeEntry> findAll();
    List<TimeEntry> findByTaskId(Long taskId);

    @Query("SELECT te FROM TimeEntry te JOIN Task t ON te.taskId = t.task_id WHERE t.projectId = :projectId")
    List<TimeEntry> findTimeEntriesByProjectId(@Param("projectId") Long projectId);
}
