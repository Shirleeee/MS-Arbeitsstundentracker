package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.Project;
import de.vfh.workhourstracker.entity.Report;
import de.vfh.workhourstracker.entity.Task;
import de.vfh.workhourstracker.entity.TimeEntry;
import de.vfh.workhourstracker.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportingService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report findReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    //das sollte besser in ProjectManagementService
    public Project findProjectUserById(Long userId) {
        //TODO
        return null;
    }

    //das sollte besser in ProjectManagementService
    public Task findTaskByUserId(Long userId) {
        //TODO
        return null;
    }

    // das sollte besser in TimeEntryService
    public TimeEntry findTimeEntryByUserId(Long userId) {
        //TODO
        return null;
    }

    public void save(Report report) {
        reportRepository.save(report);
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }
}
