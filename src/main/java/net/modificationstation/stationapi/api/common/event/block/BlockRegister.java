package net.modificationstation.stationapi.api.common.event.block;

import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.event.ModEventOld;
import net.modificationstation.stationapi.api.common.registry.ModID;

public interface BlockRegister {

    ModEventOld<BlockRegister> EVENT = new ModEventOld<>(BlockRegister.class,
            listeners ->
                    (registry, modID) -> {
                        for (BlockRegister listener : listeners)
                            listener.registerBlocks(registry, BlockRegister.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (registry, modID) -> {
                        BlockRegister.EVENT.setCurrentListener(listener);
                        listener.registerBlocks(registry, modID);
                        BlockRegister.EVENT.setCurrentListener(null);
                    },
            blockRegister ->
                    blockRegister.register((registry, modID) -> ModEventOld.post(new Data(registry)), null)
    );

    void registerBlocks(BlockRegistry registry, ModID modID);

    final class Data extends ModEventOld.Data<BlockRegister> {

        public final BlockRegistry registry;

        private Data(BlockRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
