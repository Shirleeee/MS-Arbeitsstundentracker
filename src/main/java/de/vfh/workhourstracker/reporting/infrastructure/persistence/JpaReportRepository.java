package de.vfh.workhourstracker.reporting.infrastructure.persistence;

import de.vfh.workhourstracker.reporting.domain.report.Report;
import de.vfh.workhourstracker.reporting.infrastructure.repositories.ReportRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReportRepository  extends JpaRepository<Report, Long>, ReportRepository {
}
