package net.modificationstation.stationloader.api.common.event.block;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface BlockRegister {

    Event<BlockRegister> EVENT = EventFactory.INSTANCE.newEvent(BlockRegister.class, listeners ->
            () -> {
                for (BlockRegister event : listeners)
                    event.registerBlocks();
            });

    void registerBlocks();
}
