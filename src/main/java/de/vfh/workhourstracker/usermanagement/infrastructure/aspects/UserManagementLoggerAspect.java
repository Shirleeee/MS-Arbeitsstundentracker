package de.vfh.workhourstracker.usermanagement.infrastructure.aspects;

import de.vfh.workhourstracker.projectmanagement.infrastructure.aspects.ProjectManagementLoggingAspect;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class UserManagementLoggerAspect {
    EventLogger eventLogger = new EventLogger();
    private static final Logger logger = LoggerFactory.getLogger(UserManagementLoggerAspect.class);


    @Pointcut("execution(* de.vfh.workhourstracker.usermanagement..*(..))")
    public void userManagementPackagePointcut() {}

    @Before("userManagementPackagePointcut()")
    public void logBefore() {
        logger.info("A method was called.");
    }

    @After("userManagementPackagePointcut()")
    public void logAfter() {
        logger.info("A method was executed.");
    }

    @Around("userManagementPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return eventLogger.getLoggerObject(joinPoint, logger);
    }
}
