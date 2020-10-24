package uk.co.benjiweber.expressions;

import java.util.Optional;
import java.util.function.Supplier;

public class NullSafe {
    public static <T> Optional<T> nullSafe(Supplier<T> supplier) {
        try {
            return Optional.of(supplier.get());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
