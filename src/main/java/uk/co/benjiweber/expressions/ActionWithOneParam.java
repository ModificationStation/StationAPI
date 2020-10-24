package uk.co.benjiweber.expressions;

public interface ActionWithOneParam<T,U> {
    T apply(U param) throws Exception;
}
