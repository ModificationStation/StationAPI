package net.modificationstation.stationapi.api.event.level;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;

public abstract class ChunkEvent extends LevelEvent {
    public final Chunk chunk;

    protected ChunkEvent(Level level, Chunk chunk) {
        super(level);
        this.chunk = chunk;
    }
}
