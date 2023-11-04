package net.modificationstation.stationapi.api.util.function;

import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @deprecated Use cyclops instead
 */
@Deprecated
public class Curry {

    // Bi

    public static <A, B, R> Function<A, Function<B, R>> curry(BiFunction<A, B, R> function) {
        return a -> _$(function, a);
    }

    public static <A, B, R> Function<B, Function<A, R>> incurry(BiFunction<A, B, R> function) {
        return b -> $_(function, b);
    }

    public static <A, B, R> BiFunction<A, B, R> uncurry2(Function<A, Function<B, R>> curry) {
        return (a, b) -> curry.apply(a).apply(b);
    }

    public static <A, B, R> BiFunction<A, B, R> unincurry2(Function<B, Function<A, R>> incurry) {
        return (a, b) -> incurry.apply(b).apply(a);
    }

    // Tri

    public static <A, B, C, R> Function<A, Function<B, Function<C, R>>> curry(TriFunction<A, B, C, R> function) {
        return a -> b -> __$(function, a, b);
    }

    public static <A, B, C, R> Function<C, Function<B, Function<A, R>>> incurry(TriFunction<A, B, C, R> function) {
        return c -> b -> $__(function, b, c);
    }

    public static <A, B, C, R> TriFunction<A, B, C, R> uncurry3(Function<A, Function<B, Function<C, R>>> curry) {
        return (a, b, c) -> curry.apply(a).apply(b).apply(c);
    }

    public static <A, B, C, R> TriFunction<A, B, C, R> unincurry3(Function<C, Function<B, Function<A, R>>> incurry) {
        return (a, b, c) -> incurry.apply(c).apply(b).apply(a);
    }

    // arg defaults
    // _ - arg to drop
    // $ - arg to keep

    // Bi

    public static <A, B, R> Function<B, R> _$(BiFunction<A, B, R> function, A a) {
        return b -> function.apply(a, b);
    }

    public static <A, B, R> Function<A, R> $_(BiFunction<A, B, R> function, B b) {
        return a -> function.apply(a, b);
    }

    // Tri

    public static <A, B, C, R> BiFunction<B, C, R> _$$(TriFunction<A, B, C, R> function, A a) {
        return (b, c) -> function.apply(a, b, c);
    }

    public static <A, B, C, R> BiFunction<A, C, R> $_$(TriFunction<A, B, C, R> function, B b) {
        return (a, c) -> function.apply(a, b, c);
    }

    public static <A, B, C, R> BiFunction<A, B, R> $$_(TriFunction<A, B, C, R> function, C c) {
        return (a, b) -> function.apply(a, b, c);
    }

    public static <A, B, C, R> Function<C, R> __$(TriFunction<A, B, C, R> function, A a, B b) {
        return c -> function.apply(a, b, c);
    }

    public static <A, B, C, R> Function<B, R> _$_(TriFunction<A, B, C, R> function, A a, C c) {
        return b -> function.apply(a, b, c);
    }

    public static <A, B, C, R> Function<A, R> $__(TriFunction<A, B, C, R> function, B b, C c) {
        return a -> function.apply(a, b, c);
    }
}
