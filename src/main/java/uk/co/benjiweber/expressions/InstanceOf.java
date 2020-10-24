package uk.co.benjiweber.expressions;

import java.util.Optional;

public class InstanceOf {

    public static InstanceOfBuilder when(final Object obj) {
        return new InstanceOfBuilder() {
            public <T> ThenBuilder<T> instanceOf(final Class<T> cls) {
                return new ThenBuilder<T>() {
                    public <U> ElseBuilder then(ActionWithOneParam<U, T> ifAction) {
                        return new ElseBuilder<U>(){
                            public U otherwise(U value) {
                                try {
                                    return cls.isInstance(obj) ? ifAction.apply((T)obj) : value;
                                } catch (RuntimeException | Error e) {
                                    throw e;
                                } catch (Exception e) {
                                    throw new RuntimeException( e);
                                }
                            }

                            public Optional<U> optional() {
                                try {
                                    return cls.isInstance(obj) ? Optional.of(ifAction.apply((T) obj)) : Optional.<U>empty();
                                } catch (RuntimeException | Error e) {
                                    throw e;
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                    }
                };
            }
        };
    }

    public interface InstanceOfBuilder {
        <T> ThenBuilder<T> instanceOf(Class<T> cls);
    }

    public interface ThenBuilder<T> {
        <U> ElseBuilder<U> then(ActionWithOneParam<U, T> action);
    }

    public interface ElseBuilder<U> {
        U otherwise(U value);
        Optional<U> optional();
    }
}
