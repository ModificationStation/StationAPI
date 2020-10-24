package uk.co.benjiweber.expressions.properties;

import java.util.function.Function;
import java.util.function.Supplier;

public class ExplicitName<T> extends Property<T> implements Named<T> {

    private final String name;

    ExplicitName(Supplier<T> getter, Function<T, T> setter, String name) {
        super(getter, setter);
        this.name = name;
    }

    public String name() {
        return name;
    }

}
