package net.modificationstation.stationapi.api.registry;

/**
 * A registry that allows adding or modifying values.
 * Note that in vanilla, all registries are instances of this.
 * 
 * @see Registry
 */
public interface MutableRegistry<T> extends Registry<T> {
    RegistryEntry<T> set(int rawId, RegistryKey<T> registryKey, T value);

    RegistryEntry.Reference<T> add(RegistryKey<T> registryKey, T value);

    /**
     * {@return whether the registry is empty}
     */
    boolean isEmpty();

    RegistryEntryLookup<T> createMutableEntryLookup();
}

