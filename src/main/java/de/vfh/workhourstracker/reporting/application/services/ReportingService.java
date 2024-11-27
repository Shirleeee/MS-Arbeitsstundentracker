package de.vfh.workhourstracker.reporting.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.reporting.domain.report.Report;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.reporting.domain.report.ReportSource;
import de.vfh.workhourstracker.reporting.domain.report.events.ReportCreated;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.reporting.infrastructure.repositories.ReportRepository;
import de.vfh.workhourstracker.usermanagement.domain.user.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ReportingService {
    private final ApplicationEventPublisher eventPublisher;
    private final ReportRepository reportRepository;
    EventLogger eventLogger = new EventLogger();

    @Autowired
    public ReportingService(ApplicationEventPublisher eventPublisher, ReportRepository reportRepository) {
        this.eventPublisher = eventPublisher;
        this.reportRepository = reportRepository;
    }

    public Report createReport(UserId userId, ReportSource reportSource) {
        Report report = new Report(userId, reportSource);
        report = reportRepository.save(report);

        ReportCreated event = new ReportCreated(this, report.getId(), report.getUserId(), report.getSource());
        eventPublisher.publishEvent(event);

        return report;
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
