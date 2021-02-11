package net.modificationstation.stationapi.api.common.event.level;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface SaveLevelProperties {

    GameEventOld<SaveLevelProperties> EVENT = new GameEventOld<>(SaveLevelProperties.class,
            listeners ->
                    (levelProperties, tag, spPlayerData) -> {
                        for (SaveLevelProperties listener : listeners)
                            listener.saveLevelProperties(levelProperties, tag, spPlayerData);
                    },
            (Consumer<GameEventOld<SaveLevelProperties>>) saveLevelProperties ->
                    saveLevelProperties.register((levelProperties, tag, spPlayerData) -> GameEventOld.EVENT_BUS.post(new Data(levelProperties, tag, spPlayerData)))
    );

    void saveLevelProperties(LevelProperties levelProperties, CompoundTag tag, CompoundTag spPlayerData);

    final class Data extends GameEventOld.Data<SaveLevelProperties> {

        public final LevelProperties levelProperties;
        public final CompoundTag tag;
        public final CompoundTag spPlayerData;

        private Data(LevelProperties levelProperties, CompoundTag tag, CompoundTag spPlayerData) {
            super(EVENT);
            this.levelProperties = levelProperties;
            this.tag = tag;
            this.spPlayerData = spPlayerData;
        }
    }
}
