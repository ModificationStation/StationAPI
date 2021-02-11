package net.modificationstation.stationapi.api.common.event.level;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface LoadLevelProperties {

    GameEventOld<LoadLevelProperties> EVENT = new GameEventOld<>(LoadLevelProperties.class,
            listeners ->
                    (levelProperties, tag) -> {
                        for (LoadLevelProperties listener : listeners)
                            listener.loadLevelProperties(levelProperties, tag);
                    },
            (Consumer<GameEventOld<LoadLevelProperties>>) loadLevelProperties ->
                    loadLevelProperties.register((levelProperties, tag) -> GameEventOld.EVENT_BUS.post(new Data(levelProperties, tag)))
    );

    void loadLevelProperties(LevelProperties levelProperties, CompoundTag tag);

    final class Data extends GameEventOld.Data<LoadLevelProperties> {

        public final LevelProperties levelProperties;
        public final CompoundTag tag;

        private Data(LevelProperties levelProperties, CompoundTag tag) {
            super(EVENT);
            this.levelProperties = levelProperties;
            this.tag = tag;
        }
    }
}
