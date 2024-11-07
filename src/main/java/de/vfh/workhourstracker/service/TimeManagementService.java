package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.TimeEntry;
import de.vfh.workhourstracker.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //klären: welche Formate sollen als valide gelten?
    public LocalDateTime validateStartTime(String startTime) {
        //TODO
        return null;
    }

    //klären: welche Formte sollen als valide gelten
    public LocalDateTime validateEndTime(String endTime) {
        //TODO
        return null;
    }

    //klären: soll die duration in Sekunden oder Minuten gespeichert werden oder
    //lieber in einem anderen Format?
    public Long validateDuration(Long duration) {
        //TODO
        return null;
    }
}
