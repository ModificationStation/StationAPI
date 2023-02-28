package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.LongTag;
import net.modificationstation.stationapi.api.nbt.StationNbtLong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LongTag.class)
public class MixinLongTag implements StationNbtLong {

    @Shadow public long data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof LongTag tag && data == tag.data);
    }

    @Override
    public LongTag copy() {
        return new LongTag(data);
    }
}
