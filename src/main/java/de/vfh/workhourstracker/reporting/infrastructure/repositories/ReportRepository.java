package de.vfh.workhourstracker.reporting.infrastructure.repositories;

import de.vfh.workhourstracker.reporting.domain.report.Report;
import org.springframework.stereotype.Repository;



@Repository
public interface ReportRepository {
    Report save(Report report);
}
