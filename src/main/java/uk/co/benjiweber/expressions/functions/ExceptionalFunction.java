package uk.co.benjiweber.expressions.functions;

import uk.co.benjiweber.expressions.exceptions.Exceptions;
import uk.co.benjiweber.expressions.exceptions.Result;
import uk.co.benjiweber.expressions.exceptions.WrapBuilder;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ExceptionalFunction<T, R, E extends Exception> {
    static <T, R, E extends Exception> ExceptionalFunction<T, R, E> exceptional(ExceptionalFunction<T, R, E> f) {
        return f;
    }

    R apply(T a) throws E;

    default Function<T, Optional<R>> optional() {
        return Exceptions.toOptional(this);
    }

    default Function<T, Stream<R>> stream() {
        return wrapReturn(Stream::of).wrapException(Stream::empty);
    }

    default Function<T, R> unchecked() {
        return Exceptions.unchecked(this);
    }

    default Function<T, Result<R>> resultOut() {
        return Result.wrapReturn(this);
    }

    default Function<Result<T>, Result<R>> resultInOut() {
        return Result.wrapExceptional(this);
    }

    default <V> WrapBuilder<T, V, E> wrapReturn(Function<R, V> resultWrapper) {
        return errorWrapper -> t -> {
            try {
                return resultWrapper.apply(this.apply(t));
            } catch (Exception e) {
                return errorWrapper.apply((E) e);
            }
        };
    }

    default <V> ExceptionalFunction<V, R, E> compose(ExceptionalFunction<? super V, ? extends T, E> before) {
        return (V v) -> apply(before.apply(v));
    }

    default <V> ExceptionalFunction<T, V, E> andThen(ExceptionalFunction<? super R, ? extends V, E> after) {
        return (T t) -> after.apply(apply(t));
    }

}
