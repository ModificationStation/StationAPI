package uk.co.benjiweber.expressions.functions;

public interface ExceptionalConsumer<A, E extends Exception> {
    void accept(A a) throws E;
}
