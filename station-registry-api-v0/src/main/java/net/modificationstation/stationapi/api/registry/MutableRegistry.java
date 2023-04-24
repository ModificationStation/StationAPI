package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;

/**
 * A registry that allows adding or modifying values.
 * Note that in vanilla, all registries are instances of this.
 * 
 * @see Registry
 */
public interface MutableRegistry<T> extends Registry<T> {
    RegistryEntry<T> set(int rawId, RegistryKey<T> registryKey, T value, Lifecycle lifecycle);

    RegistryEntry.Reference<T> add(RegistryKey<T> registryKey, T value, Lifecycle lifecycle);

    /**
     * {@return whether the registry is empty}
     */
    boolean isEmpty();

    RegistryEntryLookup<T> createMutableEntryLookup();

    int getNextId();
}

