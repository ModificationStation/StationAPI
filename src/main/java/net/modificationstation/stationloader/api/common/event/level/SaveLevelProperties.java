package net.modificationstation.stationloader.api.common.event.level;

import lombok.Getter;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface SaveLevelProperties {

    GameEvent<SaveLevelProperties> EVENT = new GameEvent<>(SaveLevelProperties.class,
            listeners ->
                    (levelProperties, tag, spPlayerData) -> {
                        for (SaveLevelProperties listener : listeners)
                            listener.saveLevelProperties(levelProperties, tag, spPlayerData);
                    },
            (Consumer<GameEvent<SaveLevelProperties>>) saveLevelProperties ->
                    saveLevelProperties.register((levelProperties, tag, spPlayerData) -> GameEvent.EVENT_BUS.post(new Data(levelProperties, tag, spPlayerData)))
    );

    void saveLevelProperties(LevelProperties levelProperties, CompoundTag tag, CompoundTag spPlayerData);

    final class Data extends GameEvent.Data<SaveLevelProperties> {

        @Getter
        private final LevelProperties levelProperties;
        @Getter
        private final CompoundTag tag;
        @Getter
        private final CompoundTag spPlayerData;

        private Data(LevelProperties levelProperties, CompoundTag tag, CompoundTag spPlayerData) {
            super(EVENT);
            this.levelProperties = levelProperties;
            this.tag = tag;
            this.spPlayerData = spPlayerData;
        }
    }
}
