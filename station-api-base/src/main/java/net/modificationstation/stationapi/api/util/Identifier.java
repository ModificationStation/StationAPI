package net.modificationstation.stationapi.api.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static net.modificationstation.stationapi.api.util.Namespace.MINECRAFT;

public final class Identifier implements Comparable<@NotNull Identifier> {
    @NotNull
    public static final Codec<@NotNull Identifier> CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(of(s));
        } catch (final IllegalArgumentException var2) {
            return DataResult.error(() -> "Not a valid identifier: " + s + " " + var2.getMessage());
        }
    }, Identifier::toString).stable();

    public static final char NAMESPACE_SEPARATOR = ':';

    private record IdentifierCacheKey(@NotNull Namespace namespace, @NotNull String id) {}
    @NotNull
    private static final Cache<@NotNull IdentifierCacheKey, @NotNull Identifier> CACHE = Caffeine.newBuilder().softValues().build();
    @NotNull
    private static final Function<@NotNull IdentifierCacheKey, @NotNull Identifier> IDENTIFIER_FACTORY = Identifier::new;

    public static @NotNull Identifier of(@NotNull final String identifier) {
        final int i = identifier.indexOf(NAMESPACE_SEPARATOR);
        final String namespace;
        final String path;
        if (i < 0) {
            namespace = MINECRAFT.toString();
            path = identifier;
        } else {
            namespace = identifier.substring(0, i);
            path = identifier.substring(i + 1);
        }
        return of(Namespace.of(namespace), path);
    }

    public static @NotNull Identifier of(@NotNull final Namespace namespace, @NotNull final String id) {
        return CACHE.get(new IdentifierCacheKey(namespace, id), IDENTIFIER_FACTORY);
    }

    public static @Nullable Identifier tryParse(@NotNull final String string) {
        try {
            return of(string);
        } catch (@NotNull final IllegalArgumentException e) {
            return null;
        }
    }

    public static @NotNull DataResult<@NotNull Identifier> validate(@NotNull final String id) {
        try {
            return DataResult.success(of(id));
        } catch (@NotNull final IllegalArgumentException e) {
            return DataResult.error(() -> "Not a valid identifier: " + id + " " + e.getMessage());
        }
    }

    @NotNull
    @Getter
    public final Namespace namespace;

    @NotNull
    @Getter
    public final String path;

    @NotNull
    private final String toString;
    private final int hashCode;

    private Identifier(@NotNull final IdentifierCacheKey key) {
        namespace = key.namespace;
        path = key.id;
        toString = namespace + String.valueOf(NAMESPACE_SEPARATOR) + path;
        hashCode = toString.hashCode();
    }

    public @NotNull Identifier withPath(@NotNull final String path) {
        return of(namespace, path);
    }

    public @NotNull Identifier withPath(@NotNull final UnaryOperator<String> pathFunction) {
        return withPath(pathFunction.apply(path));
    }

    public @NotNull Identifier withPrefixedPath(@NotNull final String prefix) {
        return withPath(prefix + path);
    }

    public @NotNull Identifier withSuffixedPath(@NotNull final String suffix) {
        return withPath(path + suffix);
    }

    @Override
    public int compareTo(@NotNull final Identifier o) {
        return toString.compareTo(o.toString);
    }

    @Override
    public @NotNull String toString() {
        return toString;
    }

    @Override
    public boolean equals(@NotNull final Object other) {
        if (this == other) return true;
        if (other instanceof @NotNull final Identifier otherId) {
            if (toString.equals(otherId.toString))
                throw new IllegalStateException(String.format("Encountered a duplicate instance of Identifier %s!", toString));
            return false;
        }
        return toString.equals(other);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
