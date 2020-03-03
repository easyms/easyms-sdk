package com.easyms.rest.ms.error;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@ControllerAdvice
@ConditionalOnMissingBean(annotation = ControllerAdvice.class)
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<Class, HttpStatus> CLIENT_EXCEPTIONS = new HashMap<Class, HttpStatus>() {{
        put(IllegalStateException.class, BAD_REQUEST);
        put(ResourceAccessException.class, NOT_FOUND);
    }};


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpServletRequest req, HttpClientErrorException ex) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), ex.getStatusCode(), new ServletWebRequest(req));
    }


    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Object> handleHttpServerErrorException(HttpServletRequest req, HttpServerErrorException ex) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), INTERNAL_SERVER_ERROR, new ServletWebRequest(req));
    }


    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleResourceAccessException(HttpServletRequest req, ResourceAccessException ex) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), NOT_FOUND, new ServletWebRequest(req));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(HttpServletRequest req, Exception ex) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        HttpStatus status = Optional.ofNullable(responseStatus).map(ResponseStatus::value).orElse(INTERNAL_SERVER_ERROR);
        return handleExceptionInternal(ex, null, new HttpHeaders(), status, new ServletWebRequest(req));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {

        ErrorDto errorDto = ErrorDto.builder().url(req.getRequestURL().toString())
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .errors(Lists.newArrayList(new ErrorItemDto(ex.getParameter().getParameterName(), CommonErrorMessages.method_argument_type_mismatch.getErrorKey()))).build();

        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), BAD_REQUEST, new ServletWebRequest(req));
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        List<ErrorItemDto> fieldErrorDtos = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> new ErrorItemDto(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());
        ErrorDto errorDto = ErrorDto.builder().url(url)
                .code(status.value())
                .message(status.getReasonPhrase())
                .errors(fieldErrorDtos).build();
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(HttpServletRequest req, IllegalStateException ex) {
        if (ArrayUtils.isEmpty(ex.getSuppressed())) {
            return handleExceptionInternal(ex, null, new HttpHeaders(), BAD_REQUEST, new ServletWebRequest(req));
        }
        List<Throwable> throwableList = Arrays.stream(ex.getSuppressed()).collect(Collectors.toList());
        HttpStatus httpStatus = CLIENT_EXCEPTIONS.getOrDefault(throwableList.get(0).getClass(), BAD_REQUEST);
        List<String> messages = throwableList.stream().map(Throwable::getMessage).collect(Collectors.toList());
        ErrorDto errorDto = ErrorDto.builder()
                .url(req.getRequestURL().toString())
                .code(httpStatus.value())
                .message(httpStatus.getReasonPhrase())
                .errors(Lists.newArrayList())
                .build();
        messages.forEach(errorDto::addError);
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), httpStatus, new ServletWebRequest(req));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException ex) {
        List<String> messagesKey = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).distinct().collect(Collectors.toList());
        ErrorDto errorDto = ErrorDto.builder()
                .url(req.getRequestURL().toString())
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .errors(Lists.newArrayList())
                .build();
        messagesKey.forEach(errorDto::addError);
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), BAD_REQUEST, new ServletWebRequest(req));
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        ErrorDto errorDto = ErrorDto.builder().url(url)
                .code(status.value())
                .message(status.getReasonPhrase())
                .errors(Lists.newArrayList(new ErrorItemDto(ex.getParameterName(), CommonErrorMessages.missing_required_parameters.getErrorKey()))).build();

        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), status, request);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        log(ex, status, url);
        return super.handleExceptionInternal(ex, buildBody(body, status, url), headers, status, request);
    }

    private void log(Exception ex, HttpStatus status, String url) {
        String message = "Exception while handling a Http Request URL : %s with status : %d and message : %s";
        if (status.is5xxServerError()) {
            log.error(String.format(message, url, status.value(), ex.getMessage()), ex);
        } else {
            log.warn(String.format(message, url, status.value(), ex.getMessage()), ex);
        }
    }

    private Object buildBody(Object body, HttpStatus status, String url) {
        if (Objects.isNull(body)) {
            return ErrorDto.builder().url(url).code(status.value()).message(status.getReasonPhrase()).build();
        } else if (body instanceof ErrorDto) {
            return body;
        }
        return ErrorDto.builder().url(url).code(status.value()).message(body.toString()).build();
    }
}