package net.modificationstation.stationapi.api.event.level;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.ServerChunkCache;

public class UnloadChunkEvent extends ChunkEvent {
    public static final int ID = NEXT_ID.incrementAndGet();

    public UnloadChunkEvent(Level level, Chunk chunk) {
        super(level, chunk);
    }

    @Override
    protected int getEventID() {
        return ID;
    }
}
