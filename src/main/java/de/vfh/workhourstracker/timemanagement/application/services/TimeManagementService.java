package de.vfh.workhourstracker.timemanagement.application.services;

import de.vfh.workhourstracker.projectmanagement.domain.task.TaskId;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.*;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.events.TimeTrackingEndedAndTimeEntryCreated;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.events.TimeTrackingStarted;
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

    public TimeEntry createTimeEntry(TaskId taskId, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime validStartTime = validateStartTime(startTime);
        LocalDateTime validEndTime = validateEndTime(endTime, startTime);
        Duration validTimePeriod = validateDuration(calculateDuration(startTime, endTime));

        if (validStartTime == null || validEndTime == null || validTimePeriod == null) {
            eventLogger.logError("Time entry could not be created because of invalid input.");
            return null;
        }

        TimeEntry timeEntry = new TimeEntry(taskId, new StartTime(validStartTime), new EndTime(validEndTime), new TimePeriod(validTimePeriod));
        timeEntry = timeEntryRepository.save(timeEntry);

        TimeTrackingEndedAndTimeEntryCreated event = new TimeTrackingEndedAndTimeEntryCreated(this, timeEntry.getId(), timeEntry.getTaskId(), timeEntry.getStartTime(), timeEntry.getEndTime(), timeEntry.getTimePeriod());
        eventPublisher.publishEvent(event);

        return timeEntry;
    }

    public TimeEntry startTimeTracking(TaskId taskId, LocalDateTime startTime) {
        LocalDateTime validStartTime = validateStartTime(startTime);

        if (validStartTime == null) {
            eventLogger.logError("Time entry could not be created because of invalid input.");
            return null;
        }

        TimeEntry timeEntry = new TimeEntry(taskId, new StartTime(validStartTime), null, null);
        timeEntry = timeEntryRepository.save(timeEntry);

        TimeTrackingStarted event = new TimeTrackingStarted(this, timeEntry.getId(), timeEntry.getTaskId(), timeEntry.getStartTime());
        eventPublisher.publishEvent(event);

        return timeEntry;
    }

    public TimeEntry endTimeTrackingAndCreateTimeEntry(TimeEntryId timeEntryId, LocalDateTime endTime) {
        TimeEntry existingTimeEntry = timeEntryRepository.findById(timeEntryId.getTimeEntryId()).orElse(null);
        if (existingTimeEntry == null) {
            eventLogger.logError("Time entry with ID " + timeEntryId.getTimeEntryId() + " could not be found in database.");
            return null;
        }
        LocalDateTime startTime = existingTimeEntry.getStartTime().getStartTime();

        LocalDateTime validEndTime = validateEndTime(endTime, startTime);
        Duration validDuration = validateDuration(calculateDuration(startTime, endTime));

        if (validEndTime == null || validDuration == null) {
            eventLogger.logError("Time entry could not be created because of invalid input.");
            return null;
        }

        existingTimeEntry.setEndTime(new EndTime(validEndTime));
        existingTimeEntry.setTimePeriod(new TimePeriod(validDuration));

        existingTimeEntry = timeEntryRepository.save(existingTimeEntry);

        TimeTrackingEndedAndTimeEntryCreated event = new TimeTrackingEndedAndTimeEntryCreated(this, existingTimeEntry.getId(), existingTimeEntry.getTaskId(), existingTimeEntry.getStartTime(), existingTimeEntry.getEndTime(), existingTimeEntry.getTimePeriod());
        eventPublisher.publishEvent(event);

        return existingTimeEntry;

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

    private Duration calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

    //region validation
    public LocalDateTime validateStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            eventLogger.logWarning("Startzeitpunkt darf nicht leer sein.");
            return null;
        }

        if (LocalDateTime.now().isBefore(startTime)) {
            eventLogger.logWarning("Startzeitpunkt liegt in der Zukunft.");
            return null;
        }

        return startTime;
    }

    public LocalDateTime validateEndTime(LocalDateTime endTime, LocalDateTime startTime) {
        if (endTime == null) {
            eventLogger.logWarning("Endzeitpunkt darf nicht leer sein.");
            return null;
        }

        if (LocalDateTime.now().isBefore(endTime)) {
            eventLogger.logWarning("Endzeitpunkt liegt in der Zukunft.");
            return null;
        }

        if (!startTime.isBefore(endTime)) {
            eventLogger.logWarning("Endzeitpunkt liegt vor Startzeitpunkt.");
            return null;
        }

        if (startTime.isEqual(endTime)) {
            eventLogger.logWarning("Endzeitpunkt ist gleich Startzeitpunkt.");
            return null;
        }
        return endTime;
    }

    public Duration validateDuration(Duration duration) {
        if (duration == null) {
            eventLogger.logWarning("Dauer darf nicht leer sein.");
            return null;
        }
        if (duration.toHours() > 24L) {
            eventLogger.logWarning("Dauer ist zu lang.");
            return null;
        }
        return duration;
    }
    //endregion
}
