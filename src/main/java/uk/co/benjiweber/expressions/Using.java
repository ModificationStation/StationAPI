package uk.co.benjiweber.expressions;

import java.util.function.Function;
import java.util.function.Supplier;

public class Using {
    public static <T extends AutoCloseable, R> R using(Supplier<T> closeableProvider, Function<T,R> function) {
        try (T t = closeableProvider.get()) {
            return function.apply(t);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
