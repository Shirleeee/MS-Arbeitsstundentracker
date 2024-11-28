package de.vfh.workhourstracker.timemanagement.infrastructure.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class TimeManagementLoggingAspect {
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
        long start = System.currentTimeMillis();
        logger.info("Method " + joinPoint.getSignature().toShortString() + " is being called with arguments: " + Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        long elapsedTime = System.currentTimeMillis() - start;
        logger.info("Method " + joinPoint.getSignature().toShortString() + " executed successfully in " + elapsedTime + " ms with return value: " + result);

        return result;
    }
}
