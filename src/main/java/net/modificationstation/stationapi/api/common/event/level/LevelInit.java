package net.modificationstation.stationapi.api.common.event.level;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface LevelInit {

    GameEventOld<LevelInit> EVENT = new GameEventOld<>(LevelInit.class,
            listeners ->
                    level -> {
                        for (LevelInit listener : listeners)
                            listener.onLevelInit(level);
                    },
            (Consumer<GameEventOld<LevelInit>>) levelInit ->
                    levelInit.register(level -> GameEventOld.EVENT_BUS.post(new Data(level)))
    );

    void onLevelInit(Level level);

    final class Data extends GameEventOld.Data<LevelInit> {

        public final Level level;

        private Data(Level level) {
            super(EVENT);
            this.level = level;
        }
    }
}
