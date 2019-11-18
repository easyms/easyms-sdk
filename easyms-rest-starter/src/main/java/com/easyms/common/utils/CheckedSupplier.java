package com.easyms.common.utils;

import org.springframework.util.Assert;

/**
 * @author sla.
 */
@FunctionalInterface
public interface CheckedSupplier<V> {

    @SuppressWarnings("unchecked")
    static <L extends Throwable, V> Either<L, V> atomically(CheckedSupplier<V> supplier) {
        Assert.notNull(supplier, "You better try to execute something not null !");
        try {
            return Either.right(supplier.get());
        } catch (Throwable t) {
            return Either.left((L) t);
        }
    }

    V get() throws Throwable;
}
