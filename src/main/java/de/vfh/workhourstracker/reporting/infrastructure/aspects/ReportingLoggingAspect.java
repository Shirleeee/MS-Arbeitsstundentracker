package de.vfh.workhourstracker.reporting.infrastructure.aspects;

import de.vfh.workhourstracker.shared.aspects.LoggingAspect;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReportingLoggingAspect extends LoggingAspect {
    @Override
    @Pointcut("execution(* de.vfh.workhourstracker.reporting..*(..))")
    public void applicationPackagePointcut() {}
}
