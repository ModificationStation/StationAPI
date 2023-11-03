package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.class_43;
import net.modificationstation.stationapi.api.block.BlockState;

@Cancelable
@SuperBuilder
public class BlockSetEvent extends LevelEvent {
    public final class_43 chunk;
    public final int
            x, y, z,
            blockMeta;
    public final BlockState blockState;
    public BlockState overrideState;
    public int overrideMeta;
}
