package uk.co.benjiweber.expressions.functions;

public interface ExceptionalVoid<E extends Exception> {
    void apply() throws E;
}
