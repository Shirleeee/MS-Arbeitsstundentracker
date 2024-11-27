package de.vfh.workhourstracker.timemanagement.infrastructure.repositories;

import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository {
    TimeEntry save(TimeEntry timeEntry);

    Optional<TimeEntry> findById(Long id);
    List<TimeEntry> findAll();

    void deleteById(Long id);
}
