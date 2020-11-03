package net.modificationstation.stationloader.api.common.event.block;

import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface BlockRegister {

    ModIDEvent<BlockRegister> EVENT = EventFactory.INSTANCE.newModIDEvent(BlockRegister.class, listeners ->
            () -> {
                ModIDEvent<BlockRegister> event = BlockRegister.EVENT;
                for (BlockRegister listener : listeners) {
                    event.setCurrentListener(listener);
                    listener.registerBlocks();
                }
                event.setCurrentListener(null);
            });

    void registerBlocks();
}
