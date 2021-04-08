package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.EntityHandlerRegistry;

public class EntityHandlerRegistryEvent extends RegistryEvent<EntityHandlerRegistry> {

    public EntityHandlerRegistryEvent() {
        super(EntityHandlerRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
