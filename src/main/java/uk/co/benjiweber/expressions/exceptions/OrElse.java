package uk.co.benjiweber.expressions.exceptions;

import java.util.function.Function;
import java.util.function.Supplier;

public interface OrElse<T,R,E extends Exception> {
    default Function<T,R> orElse(R value) {
        return orElse(() -> value);
    }
    Function<T,R> orElse(Supplier<R> supplier);
}
