package net.modificationstation.stationapi.api.common.event.level;

import net.minecraft.level.Level;

public class LevelInit extends LevelEvent {

    public LevelInit(Level level) {
        super(level);
    }
}
