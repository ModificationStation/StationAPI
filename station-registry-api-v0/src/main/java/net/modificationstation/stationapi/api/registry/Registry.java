package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class Registry<T> implements Iterable<Map.Entry<Identifier, T>> {

    @NotNull
    public static final Registry<Registry<?>> REGISTRIES = new RegistryRegistry(Identifier.of(MODID, "registries"));
    @NotNull
    private final Identifier registryId;
    @NotNull
    private final BiMap<Identifier, T> VALUES = HashBiMap.create();

    public Registry(@NotNull Identifier identifier) {
        this(identifier, true);
    }

    private Registry(@NotNull Identifier identifier, boolean register) {
        Objects.requireNonNull(identifier);
        this.registryId = identifier;
        if (register)
            REGISTRIES.register(registryId, this);
    }

    public void register(@NotNull Identifier identifier, @NotNull T value) {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(value);
        VALUES.put(identifier, value);
    }

    public Optional<T> get(@NotNull Identifier identifier) {
        Objects.requireNonNull(identifier);
        return Optional.ofNullable(VALUES.get(identifier));
    }

    public @NotNull Identifier getIdentifier(@NotNull T value) {
        Objects.requireNonNull(value);
        return VALUES.inverse().get(value);
    }

    @Override
    public @NotNull Iterator<Map.Entry<Identifier, T>> iterator() {
        return VALUES.entrySet().iterator();
    }

    public void forEach(@NotNull BiConsumer<Identifier, T> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<Identifier, T> identifierTEntry : this) {
            Identifier k;
            T v;
            try {
                k = identifierTEntry.getKey();
                v = identifierTEntry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    public @NotNull T registerIfAbsent(@NotNull Identifier identifier, @NotNull Function<@NotNull Identifier, @NotNull T> function) {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(function);
        return get(identifier).orElseGet(() -> {
            T value = function.apply(identifier);
            register(identifier, value);
            return value;
        });
    }

    public final @NotNull Identifier getRegistryId() {
        return registryId;
    }

    private static final class RegistryRegistry extends Registry<Registry<?>> {

        private RegistryRegistry(Identifier registryId) {
            super(registryId, false);
            register(registryId, this);
        }
    }
}
