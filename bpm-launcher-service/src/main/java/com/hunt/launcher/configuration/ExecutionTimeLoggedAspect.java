package com.hunt.bpm-launcher-service.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import lombok.extern.slf4j.Slf4j;

//@Aspect
//@Component
@Slf4j
public class ExecutionTimeLoggedAspect {

    @Around("@annotation(ExecutionTimeLogged) && execution(public * *(..))")
    public Object time(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long duration = System.currentTimeMillis() - start;

            log.info("{}.{} took {} ms", proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(), proceedingJoinPoint.getSignature().getName(), duration);
        }
    }
}
