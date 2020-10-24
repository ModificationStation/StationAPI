package uk.co.benjiweber.expressions;

import static uk.co.benjiweber.expressions.exceptions.Exceptions.unchecked;

public class ChainableVoid<T> {
    private final T obj;

    public ChainableVoid(T obj) {
        this.obj = obj;
    }

    public static <T> ChainableVoid<T> chain(T instance) {
        return new ChainableVoid<>(instance);
    }

    public ChainableVoid<T> invoke(VoidMethodOn<T> voidMethod) {
        unchecked(() -> voidMethod.invoke(obj));
        return this;
    }

    public <U> ChainableVoid<T> invoke(VoidMethodOneArgOn<T,U> method, U value) {
        unchecked(() -> method.invoke(obj, value));
        return this;
    }

    public <U,V> ChainableVoid<T> invoke(VoidMethodTwoArgOn<T,U,V> method, U value1, V value2) {
        unchecked(() -> method.invoke(obj, value1, value2));
        return this;
    }

    public interface VoidMethodOn<T> {
        void invoke(T instance) throws Exception;
    }

    public interface VoidMethodOneArgOn<T,U> {
        void invoke(T instance, U value) throws Exception;
    }

    public interface VoidMethodTwoArgOn<T,U,V> {
        void invoke(T instance, U value1, V value2) throws Exception;
    }

    public T unwrap() {
        return obj;
    }

}
