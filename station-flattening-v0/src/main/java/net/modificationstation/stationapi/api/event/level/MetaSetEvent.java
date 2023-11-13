package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.class_43;
import net.modificationstation.stationapi.api.event.world.WorldEvent;

@Cancelable
@SuperBuilder
public class MetaSetEvent extends WorldEvent {
    public final class_43 chunk;
    public final int
            x, y, z,
            blockMeta;
    public int overrideMeta;
}
