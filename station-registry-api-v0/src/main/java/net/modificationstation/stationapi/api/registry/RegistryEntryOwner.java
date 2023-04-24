package net.modificationstation.stationapi.api.registry;

/**
 * An owner of a {@link RegistryEntry} or {@link RegistryEntryList}. This is usually
 * a registry, but it is possible that an object owns multiple entries from
 * different registries.
 */
public interface RegistryEntryOwner<T> {
    default boolean ownerEquals(RegistryEntryOwner<T> other) {
        return other == this;
    }
}

