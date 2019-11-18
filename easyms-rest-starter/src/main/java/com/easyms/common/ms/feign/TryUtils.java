package com.easyms.common.ms.feign;

import com.easyms.common.ms.error.ErrorDto;
import com.easyms.common.ms.error.ErrorItemDto;
import com.easyms.common.ms.error.ErrorMessage;
import com.easyms.common.ms.exception.UnprocessedEntityException;
import com.easyms.common.ms.rest.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.CollectionUtils;


import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.*;

public class TryUtils {

    public static <T> void validate(Try<T> t, ErrorMessage down, ErrorMessage notFound) {
        Validator.of(t)
                .validateIf(TryUtils::isServiceUnavailable, () -> new UnprocessedEntityException(down.getErrorKey()))
                .ifValid()
                .validateIf(TryUtils::isNotFound, () -> new UnprocessedEntityException(notFound.getErrorKey()))
                .ifValid()
                    .validateIf(TryUtils::isForbidden, () -> new AccessDeniedException(FORBIDDEN.getReasonPhrase()))
                .execute();
    }

    public static <T> void validate(Try<T> t, ObjectMapper objectMapper, ErrorMessage down, ErrorMessage notFound) {
        Validator.of(t)
                .validateIf(TryUtils::isServiceUnavailable, () -> new UnprocessedEntityException(down.getErrorKey()))
                .ifValid()
                .validateIf(Try::isFailure, () -> new UnprocessedEntityException(getKey(extractErrorMessage(t.getThrowable(), objectMapper), notFound)))
                .execute();
    }

    private static <T> boolean isServiceUnavailable(Try<T> t) {
        return t.getStatus() == SERVICE_UNAVAILABLE.value();
    }

    private static <T> boolean isNotFound(Try<T> t) {
        return t.getStatus() == NOT_FOUND.value();
    }

    private static <T> boolean isForbidden(Try<T> t) {
        return t.getStatus() == FORBIDDEN.value();
    }

    private static String getKey(String error, ErrorMessage messages) {
        return StringUtils.isBlank(error) ? messages.getErrorKey() : error;
    }

    private static String extractErrorMessage(Throwable throwable, ObjectMapper objectMapper) {
        String content = Lists.newArrayList(StringUtils.split(throwable.getMessage(), "\n")).stream().reduce((first, second) -> second).orElse(EMPTY);
        try {
            ErrorDto errorDto = objectMapper.readValue(content, ErrorDto.class);
            return CollectionUtils.isEmpty(errorDto.getErrors()) ? EMPTY :
                    errorDto.getErrors().stream().map(ErrorItemDto::getKey).findFirst().orElse(EMPTY);
        } catch (IOException e) {
            return EMPTY;
        }
    }
}
