package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class BlockItemRegistryEvent extends RegistryEvent<ItemRegistry> {

    public BlockItemRegistryEvent() {
        super(ItemRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
