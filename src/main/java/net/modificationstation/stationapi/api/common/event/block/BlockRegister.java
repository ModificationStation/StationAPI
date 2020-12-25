package net.modificationstation.stationapi.api.common.event.block;

import lombok.Getter;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.event.ModEvent;
import net.modificationstation.stationapi.api.common.registry.ModID;

public interface BlockRegister {

    ModEvent<BlockRegister> EVENT = new ModEvent<>(BlockRegister.class,
            listeners ->
                    (registry, modID) -> {
                        for (BlockRegister listener : listeners)
                            listener.registerBlocks(registry, BlockRegister.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (blocks, modID) -> {
                        BlockRegister.EVENT.setCurrentListener(listener);
                        listener.registerBlocks(blocks, modID);
                        BlockRegister.EVENT.setCurrentListener(null);
                    },
            blockRegister ->
                    blockRegister.register((blocks, modID) -> ModEvent.post(new Data(blocks)), null)
    );

    void registerBlocks(BlockRegistry registry, ModID modID);

    final class Data extends ModEvent.Data<BlockRegister> {

        @Getter
        private final BlockRegistry registry;

        private Data(BlockRegistry blocks) {
            super(EVENT);
            registry = blocks;
        }
    }
}
