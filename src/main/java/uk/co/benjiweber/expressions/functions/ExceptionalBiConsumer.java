package uk.co.benjiweber.expressions.functions;

public interface ExceptionalBiConsumer<A, B, E extends Exception> {
    void accept(A a, B b) throws E;
}
