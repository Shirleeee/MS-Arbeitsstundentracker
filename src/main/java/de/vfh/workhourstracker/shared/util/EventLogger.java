package de.vfh.workhourstracker.shared.util;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;
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

    public Object getLoggerObject(ProceedingJoinPoint joinPoint, org.slf4j.Logger logger) throws Throwable {
        long start = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            logger.info("Method {} is being called with arguments: {}", joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
        }

        Object result = joinPoint.proceed();

        long elapsedTime = System.currentTimeMillis() - start;
        if (logger.isInfoEnabled()) {
            logger.info("Method {} executed successfully in {} ms with return value: {}", joinPoint.getSignature().toShortString(), elapsedTime, result);
        }
        return result;
    }
}
