package net.modificationstation.stationapi.impl.util;

import it.unimi.dsi.fastutil.Pair;
import lombok.val;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

/**
 * A substitute for Java's preview feature "ScopedValue".
 * <p>
 *     Part of the internal API, not to be used in other mods.
 * </p>
 */
@ApiStatus.Internal
public final class ContextVariable<T> {
    public interface ContextBuilder {
        <T> ContextBuilder where(ContextVariable<T> variable, T value);

        void run(Runnable runnable);

        <R> R get(Supplier<R> supplier);
    }

    public static <T> ContextBuilder where(ContextVariable<T> variable, T value) {
        val builder = new ContextBuilder() {
            private final List<Pair<Runnable, Runnable>> binds = new ArrayList<>();

            @Override
            public <V> ContextBuilder where(ContextVariable<V> variable, V value) {
                binds.add(Pair.of(
                        () -> variable.values.get().push(value),
                        () -> variable.values.get().pop()
                ));
                return this;
            }

            @Override
            public void run(Runnable runnable) {
                binds.stream().map(Pair::first).forEach(Runnable::run);
                try {
                    runnable.run();
                } finally {
                    binds.stream().map(Pair::second).forEach(Runnable::run);
                }
            }

            @Override
            public <R> R get(Supplier<R> supplier) {
                binds.stream().map(Pair::first).forEach(Runnable::run);
                try {
                    return supplier.get();
                } finally {
                    binds.stream().map(Pair::second).forEach(Runnable::run);
                }
            }
        };
        return builder.where(variable, value);
    }

    private final ThreadLocal<Deque<T>> values = ThreadLocal.withInitial(ArrayDeque::new);

    public T get() {
        return values.get().peek();
    }
}
