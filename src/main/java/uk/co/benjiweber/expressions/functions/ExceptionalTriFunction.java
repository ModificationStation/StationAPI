package uk.co.benjiweber.expressions.functions;

public interface ExceptionalTriFunction<A, B, C, R, E extends Exception> {
    R apply(A a, B b, C c) throws E;
}
