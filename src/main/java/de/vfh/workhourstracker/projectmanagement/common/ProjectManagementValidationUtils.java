package de.vfh.workhourstracker.projectmanagement.common;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.vfh.workhourstracker.shared.util.ErrorResponse;
import de.vfh.workhourstracker.shared.util.EventLogger;

public class ProjectManagementValidationUtils {

    private ProjectManagementValidationUtils() {
        // This constructor is intentionally empty. Nothing special is needed here.

    }

    private static final LocalDateTime MAX_END_TIME = LocalDateTime.of(2100, 12, 31, 23, 59, 59);

    public static String validateName(String name, EventLogger eventLogger) {
        return validateField(eventLogger, name, 256, "Name", "Name darf nicht leer sein.", "Name ist zu lang.");
    }

    public static String validateDescription(String description, EventLogger eventLogger) {
        return validateField(eventLogger, description, 1024, "Beschreibung", "Beschreibung darf nicht leer sein.", "Beschreibung ist zu lang.");
    }

    public static String validateDeadline(LocalDateTime deadline, EventLogger eventLogger) {
        return getValidation(deadline, eventLogger);
    }

    public static String getValidation(LocalDateTime deadline, EventLogger eventLogger) {
        return ProjectManagementValidationUtils.getValidation(deadline, eventLogger, MAX_END_TIME);
    }

    private static String validateField(EventLogger eventLogger, String field, int maxLength, String fieldName, String message, String lengthMessage) {


        if (field == null|| field.isEmpty()) {
            eventLogger.logWarning(fieldName + message);
            return message;
        }
        if (field.length() > maxLength) {
            eventLogger.logWarning(fieldName + message);
            return lengthMessage;
        }
        return "";
    }

    public static String getValidation(LocalDateTime deadline, EventLogger eventLogger, LocalDateTime maxEndTime) {
        if (deadline == null) {
            eventLogger.logWarning("Deadline darf nicht leer sein.");
            return "Deadline darf nicht leer sein.";
        }
        if (!LocalDateTime.now().isBefore(deadline)) {
            eventLogger.logWarning("Deadline darf nicht in der Vergangenheit liegen.");
            return "Deadline darf nicht in der Vergangenheit liegen.";
        }
        if (deadline.isAfter(maxEndTime)) {
            eventLogger.logWarning("Deadline darf nicht nach dem 31.12.2100 liegen.");
            return "Deadline darf nicht nach dem 31.12.2100 liegen.";
        }
        return "";
    }
    public static List<ErrorResponse> getErrorResponses(String name, String description, LocalDateTime deadline, EventLogger eventLogger, String invalid) {
        List<ErrorResponse> errors = new ArrayList<>();

        String nameError = ProjectManagementValidationUtils.validateName(name, eventLogger);
        if (!nameError.isEmpty()) {
            errors.add(new ErrorResponse(nameError, "name", invalid));
        }

        String descriptionError = ProjectManagementValidationUtils.validateDescription(description, eventLogger);
        if (!descriptionError.isEmpty()) {
            errors.add(new ErrorResponse(descriptionError, "description", invalid));
        }

        String deadlineError = ProjectManagementValidationUtils.validateDeadline(deadline, eventLogger);
        if (!deadlineError.isEmpty()) {
            errors.add(new ErrorResponse(deadlineError, "deadline", invalid));
        }

        return errors;
    }
}
