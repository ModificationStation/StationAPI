package net.modificationstation.stationapi.api.event.level;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
public class BlockSetEvent extends LevelEvent {

    @Getter
    private final boolean cancelable = true;

    public final Chunk chunk;
    public final int
            x, y, z,
            blockMeta;
    public final BlockState blockState;
    public BlockState overrideState;
    public int overrideMeta;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
