package net.modificationstation.stationapi.api.event.level;

import net.minecraft.level.Level;

public class BlockRemovedEvent extends LevelEvent {
    public static final int ID = NEXT_ID.incrementAndGet();

    public final int x;
    public final int y;
    public final int z;

    public BlockRemovedEvent(Level level, int x, int y, int z) {
        super(level);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected int getEventID() {
        return ID;
    }
}
