package net.modificationstation.stationloader.api.common.event.level;

import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface LevelInit {

    SimpleEvent<LevelInit> EVENT = new SimpleEvent<>(LevelInit.class, listeners ->
            level -> {
                for (LevelInit event : listeners)
                    event.onLevelInit(level);
            });

    void onLevelInit(Level level);
}
