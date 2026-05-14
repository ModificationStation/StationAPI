package net.modificationstation.stationapi.api.util.context;

import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Context {
    /**
     * A context that contains no keys and always returns {@code null}.
     */
    Context EMPTY = new Empty();

    /**
     * A context that signals the bypassing of conditional checks.
     */
    Context ANY = new Any();

    @SuppressWarnings("unused")
    record Key<VALUE>(Identifier id) {}

    /**
     * {@return the value associated with the given key, or {@code null} if not
     * present}
     */
    <VALUE> @Nullable VALUE get(Key<VALUE> key);

    /**
     * {@return whether this context matches all conditions unconditionally}
     * <p>
     * When {@code true}, condition evaluation should be bypassed entirely,
     * and all entries should be treated as matching. Calling {@link #get} on
     * such a context is unsupported and may throw.
     */
    default boolean matchesAll() {
        return false;
    }

    /**
     * {@return whether this context contains no keys}
     */
    default boolean isEmpty() {
        return false;
    }

    /**
     * {@return an optional containing the value associated with the given key}
     */
    default <VALUE> Optional<VALUE> getOptional(Key<VALUE> key) {
        return Optional.ofNullable(get(key));
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
        if (addition.isEmpty() || base.matchesAll()) return base;
        if (base.isEmpty() || addition.matchesAll()) return addition;
        return addition instanceof Composite composite
                ? append(append(base, composite.next), composite.head)
                : new Composite(addition, base);
    }

    /**
     * {@return a new context that includes the given key-value pair as an override}
     */
    default <VALUE> Context with(Key<VALUE> key, VALUE value) {
        return with(new Singleton<>(key, value));
    }

    /**
     * A context that contains no keys.
     */
    record Empty() implements Context {
        @Override
        public <VALUE> @Nullable VALUE get(Key<VALUE> key) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public @NotNull String toString() {
            return "EmptyContext";
        }
    }

    /**
     * A context that represents any context. Should only be used for reference
     * checks to bypass evaluations.
     */
    record Any() implements Context {
        @Override
        public <VALUE> @Nullable VALUE get(Key<VALUE> key) {
            throw new UnsupportedOperationException(
                    "Cannot get values from Context.ANY. It is only meant for evaluation bypasses."
            );
        }

        @Override
        public boolean matchesAll() {
            return true;
        }

        @Override
        public @NotNull String toString() {
            return "AnyContext";
        }
    }

    /**
     * A context containing a single key-value pair.
     */
    record Singleton<SINGLETON>(Key<SINGLETON> singletonKey, SINGLETON singletonValue) implements Context {
        @Override
        public <VALUE> @Nullable VALUE get(Key<VALUE> key) {
            // noinspection unchecked
            return singletonKey.equals(key) ? (VALUE) singletonValue : null;
        }
    }

    /**
     * A context that delegates to two other contexts.
     * <p>
     * This implementation uses an iterative walk to avoid
     * {@link StackOverflowError} on deep chains.
     */
    record Composite(Context head, Context next) implements Context {
        @Override
        public <VALUE> @Nullable VALUE get(Key<VALUE> key) {
            Context current = this;
            while (current instanceof Composite composite) {
                VALUE value = composite.head.get(key);
                if (value != null) return value;
                current = composite.next;
            }
            return current.get(key);
        }

        @Override
        public boolean matchesAll() {
            Context current = this;
            while (current instanceof Composite composite) {
                if (composite.head.matchesAll()) return true;
                current = composite.next;
            }
            return current.matchesAll();
        }

        @Override
        public boolean isEmpty() {
            Context current = this;
            while (current instanceof Composite composite) {
                if (!composite.head.isEmpty()) return false;
                current = composite.next;
            }
            return current.isEmpty();
        }
    }
}
