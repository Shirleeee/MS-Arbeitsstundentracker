package de.vfh.workhourstracker.timemanagement.application.services;

import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.*;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.events.TimeTrackingEnded;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.events.TimeTrackingStarted;
import de.vfh.workhourstracker.timemanagement.infrastructure.repositories.TimeEntryRepository;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeManagementService {
    private final ApplicationEventPublisher eventPublisher;
    private final TimeEntryRepository timeEntryRepository;

    private static final String ERROR_CODE_INVALID = "INVALID";

    @Autowired
    public TimeManagementService(ApplicationEventPublisher eventPublisher, TimeEntryRepository timeEntryRepository) {
        this.eventPublisher = eventPublisher;
        this.timeEntryRepository = timeEntryRepository;
    }

    public ResponseEntity<?> createTimeEntry(Long taskId, LocalDateTime startTime, LocalDateTime endTime) {
        String validStartTime = validateStartTime(startTime);
        String validEndTime = validateEndTime(endTime, startTime);
        String validTimePeriod = validateDuration(calculateDuration(startTime, endTime));
        Duration timePeriod = calculateDuration(startTime, endTime);

        if (!validStartTime.isEmpty() || !validEndTime.isEmpty() || !validTimePeriod.isEmpty()) {
            EventLogger.logError("Time entry could not be created because of invalid input.");

            List<ErrorResponse> errors = new ArrayList<>();
            if (!validStartTime.isEmpty()) {
                errors.add(new ErrorResponse(validStartTime, "startTime", ERROR_CODE_INVALID));
            }
            if (!validEndTime.isEmpty()) {
                errors.add(new ErrorResponse(validEndTime, "endTime", ERROR_CODE_INVALID));
            }
            if (!validTimePeriod.isEmpty()) {
                errors.add(new ErrorResponse(validTimePeriod, "timePeriod", ERROR_CODE_INVALID));
            }
            // Rückgabe der Fehlerantwort
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }
        TimeEntry timeEntry = new TimeEntry(taskId, new StartTime(startTime), new EndTime(endTime), new TimePeriod(timePeriod));
        timeEntry = timeEntryRepository.save(timeEntry);

        TimeTrackingEnded event = new TimeTrackingEnded(this, timeEntry.getId(), timeEntry.getTaskId(), timeEntry.getStartTime(), timeEntry.getEndTime(), timeEntry.getTimePeriod());
        eventPublisher.publishEvent(event);
        return ResponseEntity.ok(timeEntry);
    }

    public ResponseEntity<?> startTimeTracking(Long taskId, LocalDateTime startTime) {
        String validStartTime = validateStartTime(startTime);

        if (!validStartTime.isEmpty()) {
            EventLogger.logError("Time entry could not be created because of invalid input.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(validStartTime, "startTime", ERROR_CODE_INVALID));
        }

        TimeEntry timeEntry = new TimeEntry(taskId, new StartTime(startTime), null, new TimePeriod(Duration.ZERO));
        timeEntry = timeEntryRepository.save(timeEntry);

        TimeTrackingStarted event = new TimeTrackingStarted(this, timeEntry.getId(), timeEntry.getTaskId(), timeEntry.getStartTime());
        eventPublisher.publishEvent(event);

        return ResponseEntity.ok(timeEntry);
    }

    public ResponseEntity<?> endTimeTracking(Long timeEntryId, LocalDateTime endTime) {
        TimeEntry existingTimeEntry = timeEntryRepository.findById(timeEntryId).orElse(null);

        if (existingTimeEntry == null) {
            EventLogger.logError("Time entry with ID " + timeEntryId + " could not be found in database.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse("Time entry with ID " + timeEntryId + " could not be found in database.", "endTime", ERROR_CODE_INVALID));
        }
        LocalDateTime startTime = existingTimeEntry.getStartTime().getStartTime();

        String validEndTime = validateEndTime(endTime, startTime);
        String validDuration = validateDuration(calculateDuration(startTime, endTime));

        if (!validEndTime.isEmpty() || !validDuration.isEmpty()) {
            List<ErrorResponse> errors = new ArrayList<>();
            if (!validEndTime.isEmpty())
                errors.add(new ErrorResponse(validEndTime, "endtime", ERROR_CODE_INVALID));
            if (!validDuration.isEmpty())
                errors.add(new ErrorResponse(validDuration, "duration", ERROR_CODE_INVALID));

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

        existingTimeEntry.setEndTime(new EndTime(endTime));
        existingTimeEntry.setTimePeriod(new TimePeriod(calculateDuration(startTime, endTime)));

        existingTimeEntry = timeEntryRepository.save(existingTimeEntry);

        TimeTrackingEnded event = new TimeTrackingEnded(this, existingTimeEntry.getId(), existingTimeEntry.getTaskId(), existingTimeEntry.getStartTime(), existingTimeEntry.getEndTime(), existingTimeEntry.getTimePeriod());
        eventPublisher.publishEvent(event);

        return ResponseEntity.ok(existingTimeEntry);
    }

    public List<TimeEntry> findAllTimeEntries() {
        return timeEntryRepository.findAll();
    }

    private Duration calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

    //region validation
    public String validateStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            EventLogger.logWarning("Startzeitpunkt darf nicht leer sein.");
            return "Startzeitpunkt darf nicht leer sein.";
        }

        if (LocalDateTime.now().isBefore(startTime)) {
            EventLogger.logWarning("Startzeitpunkt liegt in der Zukunft.");
            return "Startzeitpunkt liegt in der Zukunft.";
        }

        return "";
    }

    public String validateEndTime(LocalDateTime endTime, LocalDateTime startTime) {
        if (endTime == null) {
            EventLogger.logWarning("Endzeitpunkt darf nicht leer sein.");
            return "Endzeitpunkt darf nicht leer sein.";
        }

        if (LocalDateTime.now().isBefore(endTime)) {
            EventLogger.logWarning("Endzeitpunkt liegt in der Zukunft.");
            return "Endzeitpunkt liegt in der Zukunft.";
        }

        if (startTime.isEqual(endTime)) {
            EventLogger.logWarning("Endzeitpunkt ist gleich Startzeitpunkt.");
            return "Endzeitpunkt ist gleich Startzeitpunkt.";
        }

        if (endTime.isBefore(startTime)) {
            EventLogger.logWarning("Endzeitpunkt liegt vor Startzeitpunkt.");
            return "Endzeitpunkt liegt vor Startzeitpunkt.";
        }

        return "";
    }

    public String validateDuration(Duration duration) {
        if (duration == null) {
            EventLogger.logWarning("Dauer darf nicht leer sein.");
            return "Dauer darf nicht leer sein.";
        }
        if (duration.toHours() > 24L) {
            EventLogger.logWarning("Dauer ist zu lang.");
            return "Dauer ist zu lang.";
        }
        return "";
    }
    //endregion
}
