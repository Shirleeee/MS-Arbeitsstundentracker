package de.vfh.workhourstracker.reporting.infrastructure.repositories;

import de.vfh.workhourstracker.reporting.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository {
    Report save(Report report);

    Optional<Report> findById(Long id);

    void delete(Report report);
    void deleteById(Long id);
}
