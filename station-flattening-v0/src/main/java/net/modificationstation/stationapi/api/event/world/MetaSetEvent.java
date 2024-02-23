package net.modificationstation.stationapi.api.event.world;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.world.chunk.Chunk;

@Cancelable
@SuperBuilder
public class MetaSetEvent extends WorldEvent {
    public final Chunk chunk;
    public final int
            x, y, z,
            blockMeta;
    public int overrideMeta;
}
