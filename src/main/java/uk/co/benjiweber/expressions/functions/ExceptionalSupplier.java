package uk.co.benjiweber.expressions.functions;

public interface ExceptionalSupplier<T, E extends Exception> {
    T supply() throws E;
}
