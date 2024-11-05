package de.vfh.workhourstracker.service;

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

}
