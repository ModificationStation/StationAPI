package net.modificationstation.stationloader.api.common.event.block;

import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import net.modificationstation.stationloader.api.common.registry.ModID;

public interface BlockRegister {

    ModIDEvent<BlockRegister> EVENT = EventFactory.INSTANCE.newModIDEvent(BlockRegister.class, listeners ->
            (registry, modID) -> {
                for (BlockRegister listener : listeners) {
                    BlockRegister.EVENT.setCurrentListener(listener);
                    listener.registerBlocks(registry, ModID.of(BlockRegister.EVENT.getListenerContainer(listener)));
                }
            });

    void registerBlocks(BlockRegistry registry, ModID modID);
}
