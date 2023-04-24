package net.modificationstation.stationapi.api.registry;

import java.util.List;

public enum ServerDynamicRegistryType {
    STATIC,
    RELOADABLE;

    private static final List<ServerDynamicRegistryType> VALUES;
    private static final DynamicRegistryManager.Immutable STATIC_REGISTRY_MANAGER;

    public static CombinedDynamicRegistries<ServerDynamicRegistryType> createCombinedDynamicRegistries() {
        return new CombinedDynamicRegistries<>(VALUES).with(STATIC, STATIC_REGISTRY_MANAGER);
    }

    static {
        VALUES = List.of(ServerDynamicRegistryType.values());
        STATIC_REGISTRY_MANAGER = DynamicRegistryManager.of(Registries.REGISTRIES);
    }
}

