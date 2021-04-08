package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.BlockRegistry;

public class BlockRegistryEvent extends RegistryEvent<BlockRegistry> {

    public BlockRegistryEvent() {
        super(BlockRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
