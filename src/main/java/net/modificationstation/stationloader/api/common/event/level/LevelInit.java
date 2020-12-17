package net.modificationstation.stationloader.api.common.event.level;

import lombok.Getter;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface LevelInit {

    @SuppressWarnings("UnstableApiUsage")
    GameEvent<LevelInit> EVENT = new GameEvent<>(LevelInit.class,
            listeners ->
                    level -> {
                        for (LevelInit listener : listeners)
                            listener.onLevelInit(level);
                    },
            (Consumer<GameEvent<LevelInit>>) levelInit ->
                    levelInit.register(level -> GameEvent.EVENT_BUS.post(new Data(level)))
    );

    void onLevelInit(Level level);

    final class Data extends GameEvent.Data<LevelInit> {

        @Getter
        private final Level level;

        private Data(Level level) {
            super(EVENT);
            this.level = level;
        }
    }
}
