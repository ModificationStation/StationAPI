package net.modificationstation.stationloader.api.common.event.level;

import lombok.Getter;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

public interface LevelInit {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<LevelInit> EVENT = new SimpleEvent<>(LevelInit.class,
            listeners ->
                    level -> {
                        for (LevelInit listener : listeners)
                            listener.onLevelInit(level);
                    }, (Consumer<SimpleEvent<LevelInit>>) levelInit ->
            levelInit.register(level -> SimpleEvent.EVENT_BUS.post(new Data(level)))
    );

    void onLevelInit(Level level);

    final class Data extends SimpleEvent.Data<LevelInit> {

        @Getter
        private final Level level;

        private Data(Level level) {
            super(EVENT);
            this.level = level;
        }
    }
}
