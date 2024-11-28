package de.vfh.workhourstracker.timemanagement.infrastructure.persistence;

import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTimeEntryRepository extends JpaRepository<TimeEntry, Long>, TimeEntryRepository {
}
