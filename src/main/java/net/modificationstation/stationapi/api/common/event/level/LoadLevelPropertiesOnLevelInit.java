package net.modificationstation.stationapi.api.common.event.level;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface LoadLevelPropertiesOnLevelInit {

    GameEventOld<LoadLevelPropertiesOnLevelInit> EVENT = new GameEventOld<>(LoadLevelPropertiesOnLevelInit.class,
            listeners ->
                    (levelProperties, tag) -> {
                        for (LoadLevelPropertiesOnLevelInit listener : listeners)
                            listener.loadLevelPropertiesOnLevelInit(levelProperties, tag);
                    },
            (Consumer<GameEventOld<LoadLevelPropertiesOnLevelInit>>) loadLevelPropertiesOnLevelInit ->
                    loadLevelPropertiesOnLevelInit.register((levelProperties, tag) -> GameEventOld.EVENT_BUS.post(new Data(levelProperties, tag)))
    );

    void loadLevelPropertiesOnLevelInit(LevelProperties levelProperties, CompoundTag tag);

    final class Data extends GameEventOld.Data<LoadLevelPropertiesOnLevelInit> {

        public final LevelProperties levelProperties;
        public final CompoundTag tag;

        private Data(LevelProperties levelProperties, CompoundTag tag) {
            super(EVENT);
            this.levelProperties = levelProperties;
            this.tag = tag;
        }
    }
}
