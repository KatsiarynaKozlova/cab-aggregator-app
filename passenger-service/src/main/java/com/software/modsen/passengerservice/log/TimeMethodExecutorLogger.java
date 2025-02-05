package com.software.modsen.passengerservice.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeMethodExecutorLogger {
    public static final Logger logger = LoggerFactory.getLogger(TimeMethodExecutorLogger.class);

    @Pointcut("execution(public * com.software.modsen.passengerservice.service.impl..*.*(..))")
    public void infoTimeMeasuringPublicServiceMethods() {
    }

    @Around("infoTimeMeasuringPublicServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info("Method [{}] executed in {} ms", joinPoint.getSignature(), executionTime);
        return result;
    }
}
