package uk.co.benjiweber.expressions;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Curry {

    public static <T,U,R> Function<T,Function<U,R>> curry(BiFunction<T,U,R> fun) {
        return t -> u -> fun.apply(t,u);
    }

    public interface TriFunction<T,U,V,R> {
        R apply(T t, U u, V v);
    }

    public static <T,U,V,R> Function<T,Function<U,Function<V,R>>> curry(TriFunction<T,U,V,R> fun) {
        return t -> u -> v -> fun.apply(t,u,v);
    }

    public interface QuadFunction<T,U,V,W,R> {
        R apply(T t, U u, V v, W w);
    }

    public static <T,U,V,W,R> Function<T,Function<U,Function<V,Function<W,R>>>> curry(QuadFunction<T,U,V,W,R> fun) {
        return t -> u -> v -> w -> fun.apply(t,u,v,w);
    }

}
