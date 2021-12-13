package net.modificationstation.stationapi.api.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Identifier implements Comparable<Identifier> {

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

    private Identifier(@NotNull ModID modID, @NotNull String id) {
        this.modID = modID;
        this.id = id;
        stringCache = modID + SEPARATOR + id;
    }

    public static @NotNull Identifier of(@NotNull String identifier) {
        if (!identifier.contains(SEPARATOR))
            identifier = "minecraft" + SEPARATOR + identifier;
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
        return (other instanceof String && toString().equals(other)) || (other instanceof Identifier && toString().equals(other.toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
