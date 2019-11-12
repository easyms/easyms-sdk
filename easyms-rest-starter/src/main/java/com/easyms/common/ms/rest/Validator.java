package com.easyms.common.ms.rest;


import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Validator<T> {

    private final T object;
    private final List<Throwable> exceptions = new ArrayList<>();
    private boolean doNothing;

    private Validator(T object) {
        this.object = object;
        this.doNothing = false;
    }

    public static <T> Validator<T> of(T object) {
        return new Validator<>(object);
    }

    public Validator<T> validate(Predicate<T> validation, String message) {
        if (!doNothing && !validation.test(object)) {
            exceptions.add(new IllegalStateException(message));
        }
        return this;
    }

    public Validator<T> validateIf(Predicate<T> validation, String message) {
        if (!doNothing && validation.test(object)) {
            exceptions.add(new IllegalStateException(message));
        }
        return this;
    }

    public <X extends Throwable> Validator<T> validate(Predicate<T> validation, Supplier<? extends X> exceptionSupplier) {
        if (!doNothing && !validation.test(object)) {
            exceptions.add(exceptionSupplier.get());
        }
        return this;
    }

    public <X extends Throwable> Validator<T> validateIf(Predicate<T> validation, Supplier<? extends X> exceptionSupplier) {
        if (!doNothing && validation.test(object)) {
            exceptions.add(exceptionSupplier.get());
        }
        return this;
    }



    public T get() {
        return object;
    }

    public Validator<T> ifValid() {
        resetDoNothing();
        if (CollectionUtils.isEmpty(exceptions)) {
            return this;
        }
        return execute();
    }

    public <U> Validator<T> ifMatch(U value, Predicate<U> validation) {
        if (validation.test(value)) {
            doNothing = false;
            return this;
        }
        return doNothing();
    }

    public Validator<T> execute() throws IllegalStateException {
        if (isSuccess()) {
            return this;
        }
        String globalExceptionMsgs = exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(", "));
        IllegalStateException e = new IllegalStateException(globalExceptionMsgs);
        exceptions.forEach(e::addSuppressed);
        throw e;
    }

    public static void throwException(Throwable throwable) {
        IllegalStateException e = new IllegalStateException(throwable.getMessage());
        e.addSuppressed(throwable);
        throw e;
    }

    public Validator<T> doNothing() {
        doNothing = true;
        return this;
    }

    public Validator<T> resetDoNothing() {
        doNothing = false;
        return this;
    }

    public boolean isSuccess() {
        return this.exceptions.isEmpty();
    }

}
