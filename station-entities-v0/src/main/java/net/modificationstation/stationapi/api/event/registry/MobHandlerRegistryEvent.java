package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.MobHandlerRegistry;

public class MobHandlerRegistryEvent extends RegistryEvent<MobHandlerRegistry> {

    public MobHandlerRegistryEvent() {
        super(MobHandlerRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
