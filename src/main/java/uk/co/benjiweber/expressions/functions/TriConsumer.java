package uk.co.benjiweber.expressions.functions;

public interface TriConsumer<T,U,V> {
    void accept(T t, U u, V v);
}
