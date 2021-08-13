package net.modificationstation.stationapi.api.event.level;

import net.minecraft.level.Level;

public class BlockUseEvent extends LevelEvent {
    public static final int ID = NEXT_ID.incrementAndGet();

    public BlockUseEvent(Level level, int x, int y, int z) {
        super(level);
    }

    @Override
    protected int getEventID() {
        return ID;
    }
}
