package uk.co.benjiweber.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Value<T> implements EqualsHashcode<T>, ToString<T> {
    private List<Function<T, ?>> props;

    @Override
    public boolean equals(Object other) {
        return autoEquals(other);
    }

    @Override
    public int hashCode() {
        return autoHashCode();
    }

    @Override
    public String toString() {
        return autoToString();
    }

    public List<Function<T, ?>> props() {
        return props;
    }

    public T using(Function<T,?>... props) {
        this.props = Arrays.asList(props);
        return (T) this;
    }
}
