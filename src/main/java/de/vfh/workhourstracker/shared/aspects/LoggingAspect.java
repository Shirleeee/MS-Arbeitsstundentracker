package de.vfh.workhourstracker.shared.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public abstract class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut
    public abstract void applicationPackagePointcut();

    @Before("applicationPackagePointcut()")
    public void logBefore() {
        logger.info("Es wurde eine Methode aufgerufen.");
    }

    @After("applicationPackagePointcut()")
    public void logAfter() {
        logger.info("Eine Methode wurde ausgeführt.");
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            logger.info("Methode {} wird mit folgenden Argumenten aufgerufen: {}", joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
        }

        Object result = joinPoint.proceed();

        long elapsedTime = System.currentTimeMillis() - start;
        if (logger.isInfoEnabled()) {
            logger.info("Methode {} benötigte {} ms und lieferte folgenden Rückgabewert: {}", joinPoint.getSignature().toShortString(), elapsedTime, result);
        }

        return result;
    }
}
