package uk.co.benjiweber.expressions.functions;

public interface ExceptionalTriConsumer<T,U,V,E extends Exception> {
    void accept(T t, U u, V v) throws E;
}
