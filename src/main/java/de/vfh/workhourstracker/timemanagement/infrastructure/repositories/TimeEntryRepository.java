package de.vfh.workhourstracker.timemanagement.infrastructure.repositories;

import de.vfh.workhourstracker.timemanagement.domain.model.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
}
