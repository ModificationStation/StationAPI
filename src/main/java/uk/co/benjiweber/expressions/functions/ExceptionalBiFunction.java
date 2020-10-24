package uk.co.benjiweber.expressions.functions;

public interface ExceptionalBiFunction<A, B, R, E extends Exception> {
    R apply(A a, B b) throws E;
}
