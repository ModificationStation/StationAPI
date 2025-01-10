package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.client.registry.EntityHandlerRegistry;

public class EntityHandlerRegistryEvent extends RegistryEvent<EntityHandlerRegistry> {
    public EntityHandlerRegistryEvent() {
        super(EntityHandlerRegistry.INSTANCE);
    }
}
