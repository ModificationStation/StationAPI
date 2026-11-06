package net.modificationstation.stationapi.api.client.registry;

import net.modificationstation.stationapi.api.client.entity.factory.EntityWorldAndPosFactory;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class EntityHandlerRegistry extends SimpleRegistry<EntityWorldAndPosFactory> {
    public static final RegistryKey<EntityHandlerRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("entity_handlers"));
    public static final EntityHandlerRegistry INSTANCE = Registries.create(KEY, new EntityHandlerRegistry());

    private EntityHandlerRegistry() {
        super(KEY, false);
    }
}
