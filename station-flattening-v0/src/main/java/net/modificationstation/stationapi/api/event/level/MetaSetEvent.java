package net.modificationstation.stationapi.api.event.level;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.level.chunk.Chunk;

@SuperBuilder
public class MetaSetEvent extends LevelEvent {

    @Getter
    private final boolean cancelable = true;

    public final Chunk chunk;
    public final int
            x, y, z,
            blockMeta;
    public int overrideMeta;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
