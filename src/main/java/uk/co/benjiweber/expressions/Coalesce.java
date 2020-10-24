package uk.co.benjiweber.expressions;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class Coalesce {

    public static <T> T coalesce(Supplier<T>... ts) {
        return asList(ts)
            .stream()
            .map(t -> t.get())
            .filter(t -> t != null)
            .findFirst()
            .orElse(null);
    }

    public interface AnotherSupplier<T> extends Supplier<T> {}

    public static <T> Optional<T> coalesce(AnotherSupplier<Optional<T>>... ts) {
        return asList(ts)
                .stream()
                .map(t -> t.get())
                .filter(t -> t.isPresent())
                .findFirst()
                .orElse(Optional.<T>empty());
    }

    public static <T> Optional<T> coalesce(Optional<T>... ts) {
        return asList(ts)
            .stream()
            .filter(t -> t.isPresent())
            .findFirst()
            .orElse(Optional.<T>empty());
    }

    public static <T> T coalesce(T... ts) {
        return asList(ts)
            .stream()
            .filter(t -> t != null)
            .findFirst()
            .orElse(null);
    }

}
