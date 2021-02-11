package net.modificationstation.stationapi.api.common.event.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface BlockNameSet {

    GameEventOld<BlockNameSet> EVENT = new GameEventOld<>(BlockNameSet.class,
            listeners ->
                    (block, newName) -> {
                        for (BlockNameSet listener : listeners)
                            newName = listener.getName(block, newName);
                        return newName;
                    },
            (Consumer<GameEventOld<BlockNameSet>>) blockNameSet ->
                    blockNameSet.register((block, newName) -> {
                        Data data = new Data(block, newName);
                        GameEventOld.EVENT_BUS.post(data);
                        return data.newName;
                    })
    );

    String getName(BlockBase block, String newName);

    final class Data extends GameEventOld.Data<BlockNameSet> {

        public final BlockBase block;

        public String newName;

        private Data(BlockBase block, String newName) {
            super(EVENT);
            this.block = block;
            this.newName = newName;
        }
    }
}
