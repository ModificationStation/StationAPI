package net.modificationstation.stationapi.api.registry;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public final class Identifier implements Comparable<Identifier> {

    @NotNull
    public static final String SEPARATOR = ":";

    @NotNull
    private static final Cache<String, Identifier> CACHE = CacheBuilder.newBuilder().softValues().build();

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
        try {
            return CACHE.get(identifier, () -> {
                String[] strings = identifier.split(SEPARATOR);
                String modid;
                int idIndex;
                switch(strings.length) {
                    case 1: {
                        modid = "minecraft";
                        idIndex = 0;
                        break;
                    }
                    case 2: {
                        modid = strings[0];
                        idIndex = 1;
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Invalid identifier string! " + identifier);
                }
                if (strings[idIndex].startsWith("/")) {
                    throw new IllegalArgumentException("Invalid identifier string! " + identifier);
                }
                return new Identifier(ModID.of(modid.trim()), strings[idIndex].trim());
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Identifier of(@NotNull ModID modID, @NotNull String id) {
        try {
            return CACHE.get(modID + SEPARATOR + id, () -> new Identifier(modID, id));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
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
        return (other instanceof String && toString().equals(other)) || (other instanceof Identifier && toString().equals(other.toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
