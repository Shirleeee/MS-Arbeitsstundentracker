package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.TimeEntry;
import de.vfh.workhourstracker.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TimeManagementService {

    private final TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeManagementService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    public TimeEntry findTimeEntryById(Long id) {
        return timeEntryRepository.findById(id).orElse(null);
    }

    public void save(TimeEntry timeEntry) {
        timeEntryRepository.save(timeEntry);
    }

    public void deleteById(Long id) {
        timeEntryRepository.deleteById(id);
    }

    public LocalDateTime validateStartTime(String startTime) {
        //TODO
        return null;
    }

    public LocalDateTime validateEndTime(String endTime) {
        //TODO
        return null;
    }

    public Duration validateDuration(String duration) {
        //TODO
        return null;
    }
}
