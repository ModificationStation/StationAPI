package uk.co.benjiweber.expressions.properties;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static uk.co.benjiweber.expressions.exceptions.Exceptions.unchecked;

public class GuessesName<T> extends Property<T> implements Named<T> {

    private String declaringClassName;
    private int declaringLineNumber;

    GuessesName(Supplier<T> getter, Function<T, T> setter) {
        super(getter, setter);
        recordPosition();
    }

    private void recordPosition() {
        int expectedDepth = 3;
        StackTraceElement stackTraceElement = new Throwable().fillInStackTrace().getStackTrace()[expectedDepth];
        this.declaringClassName = stackTraceElement.getClassName();
        this.declaringLineNumber = stackTraceElement.getLineNumber();
    }

    private Object createInstanceOfDeclarer() {
        Class<?> cls = unchecked(() -> Class.forName(declaringClassName));
        return unchecked(() -> cls.newInstance());
    }

    private String guessName() {
        Class<?> cls = unchecked(() -> Class.forName(declaringClassName));
        Object o = createInstanceOfDeclarer();
        Optional<Field> field = asList(cls.getDeclaredFields())
                .stream()
                .map(f -> {
                    f.setAccessible(true);
                    return f;
                })
                .filter(f -> f.getType().isAssignableFrom(GuessesName.class))
                .filter(f -> unchecked(() -> (GuessesName) f.get(o)).declaringLineNumber == this.declaringLineNumber)
                .findFirst();
        return field.get().getName();
    }

    public String name() {
        return guessName();
    }

}
