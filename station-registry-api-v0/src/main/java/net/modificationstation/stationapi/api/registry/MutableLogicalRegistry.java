package net.modificationstation.stationapi.api.registry;

public interface MutableLogicalRegistry<ENTRY> extends LogicalRegistry<ENTRY> {
    RegistryEntry.Reference<ENTRY> set(int rawId, int logicalId, RegistryKey<ENTRY> registryKey, ENTRY value);

    RegistryEntry.Reference<ENTRY> add(int logicalId, RegistryKey<ENTRY> registryKey, ENTRY value);
}
