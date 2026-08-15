package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;

public interface MutableLogicalRegistry<ENTRY> extends LogicalRegistry<ENTRY> {
    RegistryEntry.Reference<ENTRY> set(int rawId, int logicalId, RegistryKey<ENTRY> registryKey, ENTRY value, Lifecycle lifecycle);

    RegistryEntry.Reference<ENTRY> add(int logicalId, RegistryKey<ENTRY> registryKey, ENTRY value, Lifecycle lifecycle);
}
