package net.modificationstation.stationapi.api.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.modificationstation.stationapi.api.registry.ModID.MINECRAFT;

public final class Identifier implements Comparable<Identifier> {

    @NotNull
    public static final Codec<Identifier> CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(of(s));
        } catch (IllegalArgumentException var2) {
            return DataResult.error("Not a valid identifier: " + s + " " + var2.getMessage());
        }
    }, Identifier::toString).stable();

    @NotNull
    public static final String SEPARATOR = ":";

    @NotNull
    private static final Cache<String, Identifier> CACHE = Caffeine.newBuilder().softValues().build();

    @NotNull
    public final ModID modID;

    @NotNull
    public final String id;

    @NotNull
    private final String stringCache;
    private final int hashCode;

    private Identifier(@NotNull ModID modID, @NotNull String id) {
        this.modID = modID;
        this.id = id;
        stringCache = modID + SEPARATOR + id;
        hashCode = toString().hashCode();
    }

    public static @NotNull Identifier of(@NotNull String identifier) {
        if (!identifier.contains(SEPARATOR))
            identifier = MINECRAFT + SEPARATOR + identifier;
        return Objects.requireNonNull(CACHE.get(identifier, sId -> {
            String[] strings = sId.split(SEPARATOR);
            if (strings.length != 2)
                throw new IllegalArgumentException("Invalid identifier string! " + sId);
            if (strings[1].startsWith("/"))
                throw new IllegalArgumentException("Invalid identifier string! " + sId);
            return new Identifier(ModID.of(strings[0].trim()), strings[1].trim());
        }));
    }

    public static @NotNull Identifier of(@NotNull ModID modID, @NotNull String id) {
        return Objects.requireNonNull(CACHE.get(modID + SEPARATOR + id, sId -> new Identifier(modID, id)));
    }

    public static @Nullable Identifier tryParse(String string) {
        try {
            return of(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public @NotNull Identifier prepend(@NotNull String prefix) {
        return of(modID, prefix + id);
    }

    public @NotNull Identifier append(@NotNull String suffix) {
        return of(modID, id + suffix);
    }

    @Override
    public int compareTo(@NotNull Identifier o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public @NotNull String toString() {
        return stringCache;
    }

    @Override
    public boolean equals(@NotNull Object other) {
        return this == other
                || (other instanceof Identifier && toString().equals(other.toString()))
                || (other instanceof String && toString().equals(other));
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
