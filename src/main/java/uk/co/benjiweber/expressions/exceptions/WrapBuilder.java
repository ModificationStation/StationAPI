package uk.co.benjiweber.expressions.exceptions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface WrapBuilder<T,R,E extends Exception> extends OrElse<T,R,E> {

    default Function<T,R> orElse(R value) {
        return wrapException(e -> value);
    }

    default Function<T,R> orElse(Supplier<R> supplier) {
        return wrapException(supplier);
    }

    default OrElse<T,R,E> peek(Consumer<E> peeker) {
        return supplier -> wrapException(e -> {
            peeker.accept(e);
            return supplier.get();
        });
    }

    default Function<T,R> wrapException(Supplier<R> supplier) {
        return wrapException(e -> supplier.get());
    }
    Function<T,R> wrapException(Function<E,R> f);
}
