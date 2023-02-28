package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.ShortTag;
import net.modificationstation.stationapi.api.nbt.StationNbtShort;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShortTag.class)
public class MixinShortTag implements StationNbtShort {

    @Shadow public short data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof ShortTag tag && data == tag.data);
    }

    @Override
    public ShortTag copy() {
        return new ShortTag(data);
    }
}
