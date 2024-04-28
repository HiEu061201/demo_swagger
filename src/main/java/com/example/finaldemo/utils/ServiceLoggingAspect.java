package com.example.finaldemo.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.example.finaldemo.services.impl.*.*(..)) && @within(org.springframework.stereotype.Service)")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logInput(JoinPoint joinPoint) {
        logger.info("Executing service method: {}", joinPoint.getSignature().toShortString());
        logger.info("Input arguments: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logOutput(JoinPoint joinPoint, Object result) {
        logger.info("Output: {}", result);
    }
}


