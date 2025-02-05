package com.software.modsen.authservice.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class MethodLogger {
    public static final Logger logger = LoggerFactory.getLogger(MethodLogger.class);

    @Pointcut("execution(public * com.software.modsen.authservice..*.*(..)) && !within(com.software.modsen.authservice.service.*)")
    public void infoPublicMethods() {
    }

    @Pointcut("within(com.software.modsen.authservice.service.*)")
    public void timeMeasuringServiceMethod() {
    }

    @Around("infoPublicMethods()")
    public Object logMethodEntryAndExit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        logger.debug("Entering method [{}] with arguments: {}", joinPoint.getSignature(), Arrays.toString(args));
        Object result = joinPoint.proceed();
        logger.debug("Exiting method [{}] with result: {}", joinPoint.getSignature(), result);

        return result;
    }

    @Around("timeMeasuringServiceMethod()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        logger.debug("Entering method [{}] with arguments: {}", joinPoint.getSignature(), Arrays.toString(args));
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        logger.debug("Exiting method [{}] with result: {}", joinPoint.getSignature(), result);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info("Method [{}] executed in {} ms", joinPoint.getSignature(), executionTime);

        return result;
    }
}
