package de.vfh.workhourstracker.reporting.infrastructure.repositories;

import de.vfh.workhourstracker.reporting.domain.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
