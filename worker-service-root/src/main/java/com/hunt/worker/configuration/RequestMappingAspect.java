package com.hunt.worker-service-root.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class RequestMappingAspect {

    @Around("(@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping)) && execution(public * *(..))")
    public Object log(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logInvocation(proceedingJoinPoint);
    }

    private Object logInvocation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = getHttpServletRequest();

        long start = System.currentTimeMillis();

        try {
            log.info("Executing request {} {} with arguments = {}.", request.getMethod(), request.getRequestURI(), proceedingJoinPoint.getArgs());

            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long duration = System.currentTimeMillis() - start;


            log.info("{} {} from {} took {} ms.", request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), duration);
        }
    }

    HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
