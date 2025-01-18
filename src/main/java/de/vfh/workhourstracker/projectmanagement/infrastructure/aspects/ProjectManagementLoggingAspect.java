package de.vfh.workhourstracker.projectmanagement.infrastructure.aspects;

import de.vfh.workhourstracker.shared.aspects.LoggingAspect;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProjectManagementLoggingAspect extends LoggingAspect {
    @Override
    @Pointcut("execution(* de.vfh.workhourstracker.projectmanagement..*(..))")
    public void applicationPackagePointcut() {}
}
