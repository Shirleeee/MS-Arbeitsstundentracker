package de.vfh.workhourstracker.util;

import java.util.logging.Logger;
import java.util.logging.Level;

public class EventLogger {


    private static final Logger logger = Logger.getLogger(EventLogger.class.getName());

    public void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public void logError(String message) {
        logger.log(Level.SEVERE, message);
    }
}
