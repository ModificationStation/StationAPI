package net.modificationstation.stationapi.api.common.event.level.gen;

import net.minecraft.level.Level;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.common.event.level.LevelEvent;

public class LevelGenEvent extends LevelEvent {

    public final LevelSource levelSource;

    protected LevelGenEvent(Level level, LevelSource levelSource) {
        super(level);
        this.levelSource = levelSource;
    }
}
