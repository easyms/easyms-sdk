package com.easyms.security.azuread.ms.error;


import com.easyms.rest.ms.error.CommonExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@ControllerAdvice
public class SecurityExceptionHandler extends CommonExceptionHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(HttpServletRequest req, AccessDeniedException ex) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), FORBIDDEN, new ServletWebRequest(req));
    }
}
