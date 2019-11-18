package com.easyms.common.ms.feign;

import com.easyms.common.ms.error.ErrorDto;
import com.easyms.common.ms.error.ErrorMessage;
import com.easyms.common.ms.exception.UnprocessedEntityException;
import com.easyms.common.ms.rest.Validator;
import com.easyms.common.utils.Either;
import com.easyms.common.utils.StreamUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.netflix.client.ClientException;
import feign.FeignException;
import feign.RetryableException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.easyms.common.utils.CheckedSupplier.atomically;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.*;

public interface Try<T> {

    static <T> Try<T> of(Supplier<? extends T> supplier) {
        Assert.notNull(supplier, "You better try to execute something not null !");
        try {
            return new Success<>(supplier.get());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }



    T get();

    int getStatus();

    Throwable getThrowable();

    Optional<T> toOptional();

    boolean isFailure();

    boolean isSuccess();

    default T orElseGet(Supplier<T> supplier) {
        if (isFailure()) {
            return supplier.get();
        }
        return get();
    }

    default T orElseThro(ErrorMessage down) {
        Validator.of(this)
                .validateIf(this::isDown, () -> new UnprocessedEntityException(down.getErrorKey()))
                .ifValid()
                .validateIf(this::isForbidden, () -> new AccessDeniedException(FORBIDDEN.getReasonPhrase()))
                .ifValid()
                //.validateIf(this::isUnauthorized, () -> new UnauthorizedClientException(UNAUTHORIZED.getReasonPhrase()))
                //.ifValid()
                //.validateIfWithExceptions(Try::isFailure, this::getErrors)
                .execute();
        return this.get();
    }

    default T orElseThrow(Supplier<? extends Exception> exceptionSupplier) {
        Validator.of(this)
                .validateIf(Try::isFailure, exceptionSupplier)
                .execute();
        return this.get();
    }

    default List<UnprocessedEntityException> getErrors() {
        String content = Lists.newArrayList(StringUtils.split(getThrowable().getMessage(), "\n")).stream().reduce((first, second) -> second).orElse(EMPTY);

        //Either it is an ErrorDto got from another ms, or an unexpected exception
        Either<Throwable, ErrorDto> either = atomically(() -> new ObjectMapper().readValue(content, ErrorDto.class));

        return either.isRight() ?
                StreamUtils.ofNullable(either.getRight().getErrors()).map(a -> new UnprocessedEntityException(a.getKey())).collect(Collectors.toList()) :
                Lists.newArrayList(new UnprocessedEntityException(content));

    }

    default boolean isDown(Try<T> t) {
        return t.isFailure() && (isInstanceOfRetryableException(t) || t.getThrowable().getCause() instanceof ClientException);
    }

    default boolean isInstanceOfRetryableException(Try<T> t) {
        return t.getThrowable() instanceof RetryableException || t.getThrowable().getCause() instanceof RetryableException;
    }

    default boolean isForbidden(Try<T> t) {
        return t.isFailure() && t.getStatus() == FORBIDDEN.value();
    }

    default boolean isUnauthorized(Try<T> t) {
        return t.isFailure() && t.getStatus() == UNAUTHORIZED.value();
    }

    default T orElse(T other) {
        if (isFailure()) {
            return other;
        }
        return get();
    }

    /**
     * A succeeded Try.
     *
     * @param <T> component type of this Success
     */
    @Data
    final class Success<T> implements Try<T> {

        private final T value;

        /**
         * Constructs a Success.
         *
         * @param value The value of this Success.
         */
        public Success(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public int getStatus() {
            return HttpStatus.OK.value();
        }

        @Override
        public Throwable getThrowable() {
            throw new UnsupportedOperationException("No Throwable on Success");
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.of(value);
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    /**
     * A failed Try.
     *
     * @param <T> component type of this Failure
     */
    @Slf4j
    @Data
    final class Failure<T> implements Try<T> {

        private final Throwable cause;

        /**
         * Constructs a Failure.
         *
         * @param cause A cause of type Throwable, may not be null.
         * @throws NullPointerException if {@code cause} is null
         * @throws Throwable            if the given {@code cause} is fatal, i.e. non-recoverable
         */
        public Failure(Throwable cause) {
            log(cause);
            this.cause = cause;
        }

        private void log(Throwable cause) {
            if (HttpStatus.valueOf(getFeignExceptionStatus(cause)).is5xxServerError()) {
                log.error("feign error with message {}", cause.getMessage(), cause);
            } else {
                log.warn("feign error with message {}", cause.getMessage(), cause);
            }
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public int getStatus() {
            return isDown(this) ? SERVICE_UNAVAILABLE.value() : getFeignExceptionStatus(cause);
        }

        @Override
        public Throwable getThrowable() {
            return cause;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        private int getFeignExceptionStatus(Throwable cause) {
            return (cause instanceof FeignException || cause.getCause() instanceof FeignException) && ((FeignException) cause).status() != 0 ? ((FeignException) cause).status() : INTERNAL_SERVER_ERROR.value();
        }
    }

}
