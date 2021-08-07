package net.modificationstation.stationapi.api.registry;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public final class Identifier implements Comparable<Identifier> {

    @NotNull
    private static final Cache<String, Identifier> CACHE = CacheBuilder.newBuilder().softValues().build();

    @NotNull
    public final ModID modID;

    @NotNull
    public final String id;

    private Identifier(@NotNull ModID modID, @NotNull String id) {
        this.modID = modID;
        this.id = id;
    }

    public static @NotNull Identifier of(@NotNull String identifier) {
        try {
            return CACHE.get(identifier, () -> {
                String[] strings = identifier.split(":");
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
                return new Identifier(ModID.of(modid.trim()), strings[idIndex].trim());
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Identifier of(@NotNull ModID modID, @NotNull String id) {
        try {
            return CACHE.get(modID + ":" + id, () -> new Identifier(modID, id));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(@NotNull Identifier o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public @NotNull String toString() {
        return modID + ":" + id;
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
