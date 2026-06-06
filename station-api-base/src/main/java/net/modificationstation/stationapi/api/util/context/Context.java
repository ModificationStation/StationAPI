package net.modificationstation.stationapi.api.util.context;

import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

/**
 * A data source that contains keys and their associated values.
 * <p>
 * This is a functional interface that can be implemented with a lambda
 * to provide raw values based on the given {@link Identifier}.
 */
@FunctionalInterface
public interface Context {
    /**
     * A delegate interface for creating zero-allocation projections.
     */
    @FunctionalInterface
    interface Delegate extends Context {
        Context delegate();

        @Override
        default @Nullable Object getRaw(Identifier id) {
            return delegate().getRaw(id);
        }

        @Override
        default boolean contains(Identifier id) {
            return delegate().contains(id);
        }

        @Override
        default int getIntRaw(Identifier id, int defaultValue) {
            return delegate().getIntRaw(id, defaultValue);
        }
    }

    @SuppressWarnings("unused")
    record Key<VALUE>(Identifier id) {}

    /**
     * A key used to mark a context as explicitly empty/inert.
     * Composing with an empty context will yield the other context unchanged.
     */
    Key<Boolean> EMPTY_KEY = new Key<>(NAMESPACE.id("empty"));

    /**
     * An empty context that acts as an identity element for composition.
     * <p>
     * Composing with an empty context yields the other context unchanged.
     * Any {@link Delegate} wrapping an empty context is also considered empty.
     */
    Context EMPTY = id -> EMPTY_KEY.id() == id ? Boolean.TRUE : null;

    /**
     * {@return a new context containing a single key-value pair}
     */
    static <VALUE> Context of(Key<VALUE> key, VALUE value) {
        Identifier id = key.id();
        return k -> id == k ? value : null;
    }

    /**
     * {@return the raw value associated with the given identifier, or {@code null} if not
     * present}
     * <p>
     * This is the primary abstract method for implementations.
     */
    @Nullable Object getRaw(Identifier id);

    /**
     * {@return the value associated with the given key, or {@code null} if not
     * present}
     */
    @SuppressWarnings("unchecked")
    default <VALUE> @Nullable VALUE get(Key<VALUE> key) {
        return (VALUE) getRaw(key.id());
    }

    /**
     * {@return whether this context contains a value associated with the given identifier}
     */
    default boolean contains(Identifier id) {
        return getRaw(id) != null;
    }

    /**
     * {@return whether this context contains the given key}
     */
    default boolean contains(Key<?> key) {
        return contains(key.id());
    }

    /**
     * {@return an optional containing the value associated with the given identifier}
     */
    default Optional<Object> getOptional(Identifier id) {
        return Optional.ofNullable(getRaw(id));
    }

    /**
     * {@return an optional containing the value associated with the given key}
     */
    default <VALUE> Optional<VALUE> getOptional(Key<VALUE> key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * {@return the unboxed integer associated with the given key, or {@code defaultValue} if not present}
     */
    default int getInt(Key<Integer> key, int defaultValue) {
        return getIntRaw(key.id(), defaultValue);
    }

    /**
     * {@return the unboxed integer associated with the given identifier, or {@code defaultValue} if not present}
     */
    default int getIntRaw(Identifier id, int defaultValue) {
        Object raw = getRaw(id);
        return raw instanceof Integer i ? i : defaultValue;
    }

    /**
     * {@return a new context that includes the given key-value pair as an override}
     */
    default <VALUE> Context with(Key<VALUE> key, VALUE value) {
        Identifier id = key.id();
        return with(k -> id == k ? value : null);
    }

    /**
     * {@return a new context that includes the given context as an override}
     */
    default Context with(Context other) {
        return append(this, other);
    }

    /**
     * {@return a new context that includes the given contexts as overrides in the
     * provided order}
     */
    default Context with(Context... others) {
        Context result = this;
        for (Context other : others) result = result.with(other);
        return result;
    }

    private static Context append(Context base, Context addition) {
        if (addition == EMPTY || Boolean.TRUE.equals(addition.get(EMPTY_KEY))) return base;
        if (base == EMPTY || Boolean.TRUE.equals(base.get(EMPTY_KEY))) return addition;

        record Composite(Context head, Context next) implements Context {
            private Context find(Identifier id) {
                Context current = this;
                while (current instanceof Composite comp) {
                    if (comp.head.contains(id)) return comp.head;
                    current = comp.next;
                }
                return current != null && current.contains(id) ? current : null;
            }

            @Override
            public @Nullable Object getRaw(Identifier id) {
                Context ctx = find(id);
                return ctx == null ? null : ctx.getRaw(id);
            }

            @Override
            public boolean contains(Identifier id) {
                return find(id) != null;
            }

            @Override
            public int getIntRaw(Identifier id, int defaultValue) {
                Context ctx = find(id);
                return ctx == null ? defaultValue : ctx.getIntRaw(id, defaultValue);
            }
        }
        return addition instanceof Composite composite
                ? append(append(base, composite.next), composite.head)
                : new Composite(addition, base);
    }
}
