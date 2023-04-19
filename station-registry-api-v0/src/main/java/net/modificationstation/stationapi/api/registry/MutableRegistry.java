package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;

import java.util.OptionalInt;

public abstract class MutableRegistry<T> extends Registry<T> {
    public MutableRegistry(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    public abstract RegistryEntry<T> set(int var1, RegistryKey<T> var2, T var3, Lifecycle var4);

    public abstract RegistryEntry<T> add(RegistryKey<T> var1, T var2, Lifecycle var3);

    /**
     * If the given key is already present in the registry, replaces the entry associated with the given
     * key with the new entry. This method asserts that the raw ID is equal to the value already in
     * the registry. The raw ID not being present may lead to buggy behavior.
     *
     * <p>If the given key is not already present in the registry, adds the entry to the registry. If
     * {@code rawId} is present, then this method gives the entry this raw ID. Otherwise, uses the
     * next available ID.
     */
    public abstract RegistryEntry<T> replace(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") OptionalInt var1, RegistryKey<T> var2, T var3, Lifecycle var4);

    public abstract boolean isEmpty();

    public abstract int getNextId();
}

