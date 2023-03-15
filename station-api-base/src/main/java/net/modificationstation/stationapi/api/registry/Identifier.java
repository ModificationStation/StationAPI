package net.modificationstation.stationapi.api.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static net.modificationstation.stationapi.api.registry.ModID.MINECRAFT;

public final class Identifier implements Comparable<@NotNull Identifier> {

    @NotNull
    public static final Codec<@NotNull Identifier> CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(of(s));
        } catch (final IllegalArgumentException var2) {
            return DataResult.error(() -> "Not a valid identifier: " + s + " " + var2.getMessage());
        }
    }, Identifier::toString).stable();

    @NotNull
    public static final String SEPARATOR = ":";

    private record IdentifierCacheKey(@NotNull ModID namespace, @NotNull String id) {}
    @NotNull
    private static final Cache<@NotNull IdentifierCacheKey, @NotNull Identifier> CACHE = Caffeine.newBuilder().softValues().build();
    @NotNull
    private static final Function<@NotNull IdentifierCacheKey, @NotNull Identifier> IDENTIFIER_FACTORY = key -> new Identifier(key.namespace, key.id);

    @NotNull
    public final ModID modID;

    @NotNull
    public final String id;

    @NotNull
    private final String toString;
    private final int hashCode;

    private Identifier(@NotNull final ModID modID, @NotNull final String id) {
        this.modID = modID;
        this.id = id;
        toString = modID + SEPARATOR + id;
        hashCode = toString.hashCode();
    }

    public static @NotNull Identifier of(@NotNull final String identifier) {
        final int i = identifier.indexOf(SEPARATOR);
        final String namespace;
        final String path;
        if (i < 0) {
            namespace = MINECRAFT.toString();
            path = identifier;
        } else {
            namespace = identifier.substring(0, i);
            path = identifier.substring(i + 1);
        }
        return of(ModID.of(namespace), path);
    }

    public static @NotNull Identifier of(@NotNull final ModID modID, @NotNull final String id) {
        return CACHE.get(new IdentifierCacheKey(modID, id), IDENTIFIER_FACTORY);
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

    public @NotNull Identifier prepend(@NotNull final String prefix) {
        return of(modID, prefix + id);
    }

    public @NotNull Identifier append(@NotNull final String suffix) {
        return of(modID, id + suffix);
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
