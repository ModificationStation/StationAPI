package net.modificationstation.stationapi.api.registry;

import java.util.Optional;

public interface LogicalRegistry<ENTRY> extends Registry<ENTRY> {
    Optional<RegistryEntry.Reference<ENTRY>> getEntryByLogicalId(int logicalId);

    int getLogicalId(ENTRY entry);
}
