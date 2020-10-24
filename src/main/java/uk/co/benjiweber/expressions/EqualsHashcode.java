package uk.co.benjiweber.expressions;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public interface EqualsHashcode<T> {
    default boolean autoEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final T value = (T)o;
        return props().stream()
            .allMatch(prop -> Objects.equals(prop.apply((T) this), prop.apply(value)));
    }

    default int autoHashCode() {
        return props().stream()
            .map(prop -> (Object)prop.apply((T)this))
            .collect(ResultCalculator::new, ResultCalculator::accept, ResultCalculator::combine)
            .result;
    }


    static class ResultCalculator implements Consumer {
        private int result = 0;
        public void accept(Object value) {
            result = 31 * result + (value != null ? value.hashCode() : 0);
        }
        public void combine(ResultCalculator other) {
            result += other.result;
        }
    }

    List<Function<T,?>> props();
}
