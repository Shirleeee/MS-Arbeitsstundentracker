package de.vfh.workhourstracker.shared.util;

import java.util.logging.Logger;
import java.util.logging.Level;

public class EventLogger {
    private static final Logger logger = Logger.getLogger(EventLogger.class.getName());

    private EventLogger() {
        //Instanziierung nicht notwendig, da alle Methoden statisch sind.
        throw new IllegalStateException("Utility-Klasse, Instanziierung nicht erlaubt.");
    }

    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void logError(String message) {
        logger.log(Level.SEVERE, message);
    }
}
