package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.DoubleTag;
import net.modificationstation.stationapi.api.nbt.StationNbtDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DoubleTag.class)
public class MixinDoubleTag implements StationNbtDouble {

    @Shadow public double data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof DoubleTag tag && data == tag.data);
    }

    @Override
    public DoubleTag copy() {
        return new DoubleTag(data);
    }
}
