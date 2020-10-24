package uk.co.benjiweber.expressions;

public interface Action<T>  {
    public T apply() throws Exception;
}
