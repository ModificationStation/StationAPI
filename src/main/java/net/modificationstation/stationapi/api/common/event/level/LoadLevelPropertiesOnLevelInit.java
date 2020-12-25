package net.modificationstation.stationapi.api.common.event.level;

import lombok.Getter;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface LoadLevelPropertiesOnLevelInit {

    GameEvent<LoadLevelPropertiesOnLevelInit> EVENT = new GameEvent<>(LoadLevelPropertiesOnLevelInit.class,
            listeners ->
                    (levelProperties, tag) -> {
                        for (LoadLevelPropertiesOnLevelInit listener : listeners)
                            listener.loadLevelPropertiesOnLevelInit(levelProperties, tag);
                    },
            (Consumer<GameEvent<LoadLevelPropertiesOnLevelInit>>) loadLevelPropertiesOnLevelInit ->
                    loadLevelPropertiesOnLevelInit.register((levelProperties, tag) -> GameEvent.EVENT_BUS.post(new Data(levelProperties, tag)))
    );

    void loadLevelPropertiesOnLevelInit(LevelProperties levelProperties, CompoundTag tag);

    final class Data extends GameEvent.Data<LoadLevelPropertiesOnLevelInit> {

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
