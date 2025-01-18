package de.vfh.workhourstracker.usermanagement.infrastructure.aspects;

import de.vfh.workhourstracker.shared.aspects.LoggingAspect;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserManagementLoggingAspect extends LoggingAspect {
    @Override
    @Pointcut("execution(* de.vfh.workhourstracker.usermanagement..*(..))")
    public void applicationPackagePointcut() {}
}
