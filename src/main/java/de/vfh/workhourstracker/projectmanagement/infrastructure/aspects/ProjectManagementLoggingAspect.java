package de.vfh.workhourstracker.projectmanagement.infrastructure.aspects;

import de.vfh.workhourstracker.shared.util.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProjectManagementLoggingAspect {
    EventLogger eventLogger = new EventLogger();
    private static final Logger logger = LoggerFactory.getLogger(ProjectManagementLoggingAspect.class);

    @Pointcut("execution(* de.vfh.workhourstracker.projectmanagement..*(..))")
    public void projectManagementPackagePointcut() {}

    @Before("projectManagementPackagePointcut()")
    public void logBefore() {
        logger.info("A method was called.");
    }

    @After("projectManagementPackagePointcut()")
    public void logAfter() {
        logger.info("A method was executed.");
    }

    @Around("projectManagementPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return eventLogger.getLoggerObject(joinPoint, logger);
    }


}
