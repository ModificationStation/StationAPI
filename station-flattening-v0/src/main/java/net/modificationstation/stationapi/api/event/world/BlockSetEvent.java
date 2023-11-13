package net.modificationstation.stationapi.api.event.world;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.class_43;
import net.modificationstation.stationapi.api.block.BlockState;

@Cancelable
@SuperBuilder
public class BlockSetEvent extends WorldEvent {
    public final class_43 chunk;
    public final int
            x, y, z,
            blockMeta;
    public final BlockState blockState;
    public BlockState overrideState;
    public int overrideMeta;
}
