package net.modificationstation.stationapi.api.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mojang.serialization.Codec;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Optional;

/**
 * Represents a key for a value in a registry in a context where a
 * root registry is available.
 * 
 * @param <T> the type of the value
 * @see Registries#ROOT
 */
@SuppressWarnings("unused")
public class RegistryKey<T> {
    /**
     * A cache of all registry keys ever created.
     */
    private static final Cache<RegistryIdPair, RegistryKey<?>> CACHE = Caffeine.newBuilder().softValues().build();
    /**
     * The identifier of the registry in the root registry.
     */
    private final Identifier registry;
    /**
     * The identifier of the value in the registry specified by {@link #registry}.
     */
    private final Identifier value;

    public static <T> Codec<RegistryKey<T>> createCodec(RegistryKey<? extends Registry<T>> registry) {
        return Identifier.CODEC.xmap(id -> RegistryKey.of(registry, id), RegistryKey::getValue);
    }

    /**
     * Creates a registry key for a value in a registry with a registry key for
     * the value-holding registry in the root registry and an identifier of the
     * value.
     * 
     * <p>You can call it like {@code RegistryKey.of(Registry.ITEM_KEY, new Identifier("iron_ingot"))}
     * to create a registry key for iron ingot.
     * 
     * @param <T> the type of the value
     * 
     * @param value the identifier of the value
     * @param registry the registry key of the registry in the root registry
     */
    public static <T> RegistryKey<T> of(RegistryKey<? extends Registry<T>> registry, Identifier value) {
        return RegistryKey.of(registry.value, value);
    }

    /**
     * Creates a registry key for a registry in the root registry (registry of
     * all registries) with an identifier for the registry.
     * 
     * <p>You can call it like {@code RegistryKey.of(new Identifier("block"))}
     * to create a registry key for the block registry.
     * 
     * @param <T> the element type of the registry
     * 
     * @param registry the identifier of the registry
     */
    public static <T, R extends Registry<T>> RegistryKey<R> ofRegistry(Identifier registry) {
        return RegistryKey.of(Registries.ROOT_KEY, registry);
    }

    private static <T> RegistryKey<T> of(Identifier registry, Identifier value) {
        //noinspection unchecked
        return (RegistryKey<T>) CACHE.get(new RegistryIdPair(registry, value), pair -> new RegistryKey<>(pair.registry, pair.id));
    }

    private RegistryKey(Identifier registry, Identifier value) {
        this.registry = registry;
        this.value = value;
    }

    public String toString() {
        return "ResourceKey[" + this.registry + " / " + this.value + "]";
    }

    /**
     * Returns whether this registry key belongs to the given registry (according to its type, not whether the registry actually contains this key).
     * 
     * @param registry the key of the registry that this registry key should be inside
     */
    public boolean isOf(RegistryKey<? extends Registry<?>> registry) {
        return this.registry.equals(registry.getValue());
    }

    /**
     * {@return {@code Optional.of(this)} if the key is of {@code registryRef},
     * otherwise {@link Optional#empty}}
     * 
     * @apiNote This can be used to safely cast an unknown key to {@code RegistryKey<E>}
     * by passing the registry {@code E}.
     */
    public <E> Optional<RegistryKey<E>> tryCast(RegistryKey<? extends Registry<E>> registryRef) {
        //noinspection unchecked
        return this.isOf(registryRef) ? Optional.of((RegistryKey<E>) this) : Optional.empty();
    }

    public Identifier getValue() {
        return this.value;
    }

    public Identifier getRegistry() {
        return this.registry;
    }

    record RegistryIdPair(Identifier registry, Identifier id) {}
}

