package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeManagementService {

    private final TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeManagementService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }
}
