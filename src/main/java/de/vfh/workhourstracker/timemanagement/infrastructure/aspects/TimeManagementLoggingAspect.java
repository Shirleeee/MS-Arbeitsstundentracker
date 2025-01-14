package de.vfh.workhourstracker.timemanagement.infrastructure.aspects;

import de.vfh.workhourstracker.projectmanagement.infrastructure.aspects.ProjectManagementLoggingAspect;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class TimeManagementLoggingAspect {
    EventLogger eventLogger = new EventLogger();
    private static final Logger logger = LoggerFactory.getLogger(TimeManagementLoggingAspect.class);

    @Pointcut("execution(* de.vfh.workhourstracker.timemanagement..*(..))")
    public void timeManagementPackagePointcut() {}

    @Before("timeManagementPackagePointcut()")
    public void logBefore() {
        logger.info("A method was called.");
    }

    @After("timeManagementPackagePointcut()")
    public void logAfter() {
        logger.info("A method was executed.");
    }

    @Around("timeManagementPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return eventLogger.getLoggerObject(joinPoint, logger);
    }

}
