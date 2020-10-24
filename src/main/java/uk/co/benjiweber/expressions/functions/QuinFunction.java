package uk.co.benjiweber.expressions.functions;

public interface QuinFunction<A, B, C, D, E, R> {
    R apply(A a, B b, C c, D d, E e);
}
