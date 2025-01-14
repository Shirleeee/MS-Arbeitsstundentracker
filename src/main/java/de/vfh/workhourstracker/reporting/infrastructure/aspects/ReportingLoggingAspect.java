package de.vfh.workhourstracker.reporting.infrastructure.aspects;

import de.vfh.workhourstracker.projectmanagement.infrastructure.aspects.ProjectManagementLoggingAspect;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ReportingLoggingAspect {
    EventLogger eventLogger = new EventLogger();
    private static final Logger logger = LoggerFactory.getLogger(ReportingLoggingAspect.class);

    @Pointcut("execution(* de.vfh.workhourstracker.reporting..*(..))")
    public void reportingPackagePointcut() {}

    @Before("reportingPackagePointcut()")
    public void logBefore() {
        logger.info("A method was called.");
    }

    @After("reportingPackagePointcut()")
    public void logAfter() {
        logger.info("A method was executed.");
    }

    @Around("reportingPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return eventLogger.getLoggerObject(joinPoint, logger);
    }


}
