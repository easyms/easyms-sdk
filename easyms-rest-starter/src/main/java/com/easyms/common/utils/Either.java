package com.easyms.common.utils;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

/**
 * Either represents a value of two possible types. An Either is either a {@link Left} or a {@link Right}.
 * <p>
 * If the given Either is a Right and projected to a Left, the Left operations have no effect on the Right value.<br>
 * If the given Either is a Left and projected to a Right, the Right operations have no effect on the Left value.<br>
 * If a Left is projected to a Left or a Right is projected to a Right, the operations have an effect.
 *
 * <strong>Example:</strong> A compute() function, which results either in an Integer value (in the case of success) or
 * in an error message of type String (in the case of failure). By convention the success case is Right and the failure
 * is Left.
 *
 * <pre>
 * <code>
 * Either&lt;String,Integer&gt; value = compute().right().map(i -&gt; i * 2).toEither();
 * </code>
 * </pre>
 * <p>
 * If the result of compute() is Right(1), the value is Right(2).<br>
 * If the result of compute() is Left("error"), the value is Left("error").
 *
 * @param <L> The type of the Left value of an Either.
 * @param <R> The type of the Right value of an Either.
 * @author sla.
 */
public interface Either<L, R> {

    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    L getLeft();

    R getRight();

    boolean isLeft();

    boolean isRight();

    @SuppressWarnings("unchecked")
    default <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isRight() ? Either.right(mapper.apply(getRight())) : (Either<L, U>) this;
    }

    default R orElse(R other) {
        return isRight() ? getRight() : other;
    }

    default <X extends Throwable> R orElseThrow(Function<? super L, X> exceptionFunction) throws X {
        Objects.requireNonNull(exceptionFunction, "exceptionFunction is null");
        if (isRight()) {
            return getRight();
        }
        throw exceptionFunction.apply(getLeft());
    }

    @AllArgsConstructor
    final class Right<L, R> implements Either<L, R>, Serializable {

        private final R value;

        @Override
        public R getRight() {
            return value;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("getLeft() on Right");
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public boolean isLeft() {
            return false;
        }
    }

    @AllArgsConstructor
    final class Left<L, R> implements Either<L, R>, Serializable {

        private final L value;

        @Override
        public R getRight() {
            throw new NoSuchElementException("getRight() on Left");
        }

        @Override
        public L getLeft() {
            return value;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public boolean isLeft() {
            return true;
        }
    }
}
