package de.vfh.workhourstracker.projectmanagement.utils;

import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidationUtils {
    private static final EventLogger eventLogger = new EventLogger();

    private static final String ERROR_CODE_INVALID = "INVALID";
    private static final LocalDateTime MAX_END_TIME = LocalDateTime.of(2100, 12, 31, 23, 59, 59);

    private ValidationUtils() {}

    public static ResponseEntity<?> validateObject(String name, String description, LocalDateTime deadline) {
        String validatedName = validateName(name);
        String validatedDescription = validateDescription(description);
        String validatedDeadline = validateDeadline(deadline);

        if (!validatedName.isEmpty() || !validatedDescription.isEmpty() || !validatedDeadline.isEmpty()) {
            List<ErrorResponse> errors = new ArrayList<>();
            if (!validatedName.isEmpty()) {
                errors.add(new ErrorResponse(validatedName, "name", ERROR_CODE_INVALID));
            }
            if (!validatedDescription.isEmpty()) {
                errors.add(new ErrorResponse(validatedDescription, "description", ERROR_CODE_INVALID));
            }
            if (!validatedDeadline.isEmpty()) {
                errors.add(new ErrorResponse(validatedDeadline, "deadline", ERROR_CODE_INVALID));
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }
        return null;
    }

    private static String validateName(String name){
        if (name == null || name.isEmpty()) {
            eventLogger.logWarning("Name darf nicht leer sein.");
            return "Name darf nicht leer sein.";
        }
        if (name.length() > 256) {
            eventLogger.logWarning("Name ist zu lang.");
            return "Name ist zu lang.";
        }
        return "";
    }

    private static String validateDescription(String description){
        //TODO: Warum darf die Description nicht null sein?
        if (description == null || description.isEmpty()) {
            eventLogger.logWarning("Beschreibung darf nicht leer sein.");
            return "Beschreibung darf nicht leer sein.";
        }
        if (description.length() > 1024) {
            eventLogger.logWarning("Beschreibung ist zu lang.");
            return "Beschreibung ist zu lang.";
        }
        return "";
    }

    private static String validateDeadline(LocalDateTime deadline) {
        if (deadline == null) {
            eventLogger.logWarning("Deadline darf nicht leer sein.");
            return "Deadline darf nicht leer sein.";
        }
        if (!LocalDateTime.now().isBefore(deadline)) {
            eventLogger.logWarning("Deadline darf nicht in der Vergangenheit liegen.");
            return "Deadline darf nicht in der Vergangenheit liegen.";
        }
        if (deadline.isAfter(MAX_END_TIME)) {
            //TODO: Datum im String durch MAX_END_TIME ersetzen
            eventLogger.logWarning("Deadline darf nicht nach dem 31.12.2100 liegen.");
            return "Deadline darf nicht nach dem 31.12.2100 liegen.";
        }
        return "";
    }



}
