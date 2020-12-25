package net.modificationstation.stationapi.api.common.event.level;

import lombok.Getter;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface LoadLevelProperties {

    GameEvent<LoadLevelProperties> EVENT = new GameEvent<>(LoadLevelProperties.class,
            listeners ->
                    (levelProperties, tag) -> {
                        for (LoadLevelProperties listener : listeners)
                            listener.loadLevelProperties(levelProperties, tag);
                    },
            (Consumer<GameEvent<LoadLevelProperties>>) loadLevelProperties ->
                    loadLevelProperties.register((levelProperties, tag) -> GameEvent.EVENT_BUS.post(new Data(levelProperties, tag)))
    );

    void loadLevelProperties(LevelProperties levelProperties, CompoundTag tag);

    final class Data extends GameEvent.Data<LoadLevelProperties> {

        @Getter
        private final LevelProperties levelProperties;
        @Getter
        private final CompoundTag tag;

        private Data(LevelProperties levelProperties, CompoundTag tag) {
            super(EVENT);
            this.levelProperties = levelProperties;
            this.tag = tag;
        }
    }
}
