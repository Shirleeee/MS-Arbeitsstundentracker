package de.vfh.workhourstracker.timemanagement.infrastructure.aspects;

import de.vfh.workhourstracker.shared.aspects.LoggingAspect;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeManagementLoggingAspect extends LoggingAspect {
    @Override
    @Pointcut("execution(* de.vfh.workhourstracker.timemanagement..*(..))")
    public void applicationPackagePointcut() {}

}
