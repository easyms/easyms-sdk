package com.easyms.sampleapp.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
@AllArgsConstructor
public class JwtDecoderInterceptor {

    private final InternalTokenProperties internalTokenProperties;


    @Around(value = "bean(jwtDecoder)")
    public Object interceptJwtDecode(final ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("intercept jwtDecoder");

        return joinPoint.proceed();
    }
}
