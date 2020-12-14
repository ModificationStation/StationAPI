package uk.co.benjiweber.expressions;

public interface Action<T> {
    T apply() throws Exception;
}
