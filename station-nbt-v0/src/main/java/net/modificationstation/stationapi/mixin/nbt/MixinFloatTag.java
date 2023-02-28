package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.FloatTag;
import net.modificationstation.stationapi.api.nbt.StationNbtFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FloatTag.class)
public class MixinFloatTag implements StationNbtFloat {

    @Shadow public float data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof FloatTag tag && data == tag.data);
    }

    @Override
    public FloatTag copy() {
        return new FloatTag(data);
    }
}
