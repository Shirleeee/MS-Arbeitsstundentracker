package de.vfh.workhourstracker.timemanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.task.TaskId;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.EndTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.StartTime;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimePeriod;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.events.TimeEntryCreated;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TimeManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final TimeEntryRepository timeEntryRepository;
    EventLogger eventLogger = new EventLogger();

    @Autowired
    public TimeManagementService(ApplicationEventPublisher eventPublisher, TimeEntryRepository timeEntryRepository) {
        this.eventPublisher = eventPublisher;
        this.timeEntryRepository = timeEntryRepository;
    }

    public TimeEntry createTimeEntry(TaskId taskId, String startTime, String endTime, String timePeriod) {
        LocalDateTime validStartTime = validateStartTime(startTime);
        LocalDateTime validEndTime = validateEndTime(endTime, startTime);
        Duration validTimePeriod = validateDuration(timePeriod);

        if (validStartTime == null || validEndTime == null || validTimePeriod == null) {
            eventLogger.logError("Time entry could not be created because of invalid input.");
            return null;
        }

        TimeEntry timeEntry = new TimeEntry(taskId, new StartTime(validStartTime), new EndTime(validEndTime), new TimePeriod(validTimePeriod));
        timeEntry = timeEntryRepository.save(timeEntry);

        TimeEntryCreated event = new TimeEntryCreated(this, timeEntry.getId(), timeEntry.getTaskId(), timeEntry.getStartTime(), timeEntry.getEndTime(), timeEntry.getTimePeriod());
        eventPublisher.publishEvent(event);

        return timeEntry;
    }

    public TimeEntry findTimeEntryById(Long id) {
        return timeEntryRepository.findById(id).orElse(null);
    }

    public List<TimeEntry> findAllTimeEntries() {
        return timeEntryRepository.findAll();
    }

    public void save(TimeEntry timeEntry) {
        timeEntryRepository.save(timeEntry);
    }

    public void deleteById(Long id) {
        timeEntryRepository.deleteById(id);
    }

    //region validation
    public LocalDateTime validateStartTime(String startTime) {
        if (startTime == null) {
            eventLogger.logWarning("Startzeitpunkt darf nicht leer sein.");
            return null;
        } else {
            String startTimePattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}";

            if (!startTime.matches(startTimePattern)) {
                eventLogger.logWarning("Startzeitpunkt ist nicht valide.");
                return null;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dateTimeStartTime = LocalDateTime.parse(startTime, formatter);
//! Doppelter Code
            LocalDateTime now = LocalDateTime.now();

            if (now.isBefore(dateTimeStartTime)) {
                eventLogger.logWarning("Startzeitpunkt liegt in der Zukunft.");
                return null;
            }

            return dateTimeStartTime;
        }

    }

    public LocalDateTime validateEndTime(String endTime, String startTime) {
        if (endTime == null || endTime.isEmpty()) {
            eventLogger.logWarning("Endzeitpunkt darf nicht leer sein.");
            return null;
        }

        String endTimePattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}";

        if (!endTime.matches(endTimePattern)) {
            eventLogger.logWarning("Endzeitpunkt ist nicht valide.");
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTimeEndTime = LocalDateTime.parse(endTime, formatter);
//! Doppelter Code
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(dateTimeEndTime)) {
            eventLogger.logWarning("Endzeitpunkt liegt in der Zukunft.");
            return null;
        }


        LocalDateTime dateTimeStartTime = LocalDateTime.parse(startTime, formatter);

        if (!dateTimeStartTime.isBefore(dateTimeEndTime)) {
            eventLogger.logWarning("Endzeitpunkt liegt vor Startzeitpunkt.");
            return null;
        }

        if (dateTimeStartTime.isEqual(dateTimeEndTime)) {
            eventLogger.logWarning("Endzeitpunkt ist gleich Startzeitpunkt.");
            return null;
        }
        return dateTimeEndTime;
    }

    public Duration validateDuration(String duration) {
        if (duration == null || duration.isEmpty()) {
            eventLogger.logWarning("Dauer darf nicht leer sein.");
            return null;
        }
        String durationPattern = "^P(T(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?)$";

        if (!duration.matches(durationPattern)) {
            eventLogger.logWarning("Dauer ist nicht valide.");
            return null;
        }
        Duration parsedDuration = Duration.parse(duration);
        if (parsedDuration.toHours() > 24L) {
            eventLogger.logWarning("Dauer ist zu lang.");
            return null;
        }

        return Duration.parse(duration);
    }
    //endregion
}
