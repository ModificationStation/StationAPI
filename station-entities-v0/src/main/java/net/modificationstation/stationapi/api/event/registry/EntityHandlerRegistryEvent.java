package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.client.entity.factory.EntityWorldAndPosFactory;
import net.modificationstation.stationapi.api.client.registry.EntityHandlerRegistry;

public class EntityHandlerRegistryEvent extends RegistryEvent.EntryTypeBound<EntityWorldAndPosFactory, EntityHandlerRegistry> {
    public EntityHandlerRegistryEvent() {
        super(EntityHandlerRegistry.INSTANCE);
    }
}
