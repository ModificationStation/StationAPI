package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.DimensionRegistry;

public class DimensionRegistryEvent extends RegistryEvent<DimensionRegistry> {

    public DimensionRegistryEvent() {
        super(DimensionRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
