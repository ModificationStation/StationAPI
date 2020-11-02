package net.modificationstation.stationloader.api.common.event.block;

import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface BlockRegister {

    ModIDEvent<BlockRegister> EVENT = EventFactory.INSTANCE.newModIDEvent(BlockRegister.class, listeners ->
            () -> {
                for (BlockRegister event : listeners) {
                    BlockRegister.EVENT.setCurrentListener(event);
                    event.registerBlocks();
                }
                BlockRegister.EVENT.setCurrentListener(null);
            });

    void registerBlocks();
}
