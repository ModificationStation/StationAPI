package net.modificationstation.stationapi.api.registry;

import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.Optional;

public interface RegistryEntryLookup<T> {
    Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> var1);

    default RegistryEntry.Reference<T> getOrThrow(RegistryKey<T> key) {
        return this.getOptional(key).orElseThrow(() -> new IllegalStateException("Missing element " + key));
    }

    Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> var1);

    default RegistryEntryList.Named<T> getOrThrow(TagKey<T> tag) {
        return this.getOptional(tag).orElseThrow(() -> new IllegalStateException("Missing tag " + tag));
    }

    interface RegistryLookup {
        <T> Optional<RegistryEntryLookup<T>> getOptional(RegistryKey<? extends Registry<? extends T>> var1);

        default <T> RegistryEntryLookup<T> getOrThrow(RegistryKey<? extends Registry<? extends T>> registryRef) {
            return this.getOptional(registryRef).orElseThrow(() -> new IllegalStateException("Registry " + registryRef.getValue() + " not found"));
        }
    }
}

