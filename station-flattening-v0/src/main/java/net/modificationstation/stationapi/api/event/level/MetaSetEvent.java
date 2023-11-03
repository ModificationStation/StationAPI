package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.class_43;

@Cancelable
@SuperBuilder
public class MetaSetEvent extends LevelEvent {
    public final class_43 chunk;
    public final int
            x, y, z,
            blockMeta;
    public int overrideMeta;
}
