package net.modificationstation.stationloader.api.common.event.block;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface BlockNameSet {

    GameEvent<BlockNameSet> EVENT = new GameEvent<>(BlockNameSet.class,
            listeners ->
                    (block, newName) -> {
                        for (BlockNameSet listener : listeners)
                            newName = listener.getName(block, newName);
                        return newName;
                    },
            (Consumer<GameEvent<BlockNameSet>>) blockNameSet ->
                    blockNameSet.register((block, newName) -> {
                        Data data = new Data(block, newName);
                        GameEvent.EVENT_BUS.post(data);
                        return data.getNewName();
                    })
    );

    String getName(BlockBase block, String newName);

    final class Data extends GameEvent.Data<BlockNameSet> {

        @Getter
        private final BlockBase block;
        @Getter
        @Setter
        private String newName;

        private Data(BlockBase block, String newName) {
            super(EVENT);
            this.block = block;
            this.newName = newName;
        }
    }
}
