package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtFloat;
import net.modificationstation.stationapi.api.nbt.StationNbtFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtFloat.class)
public class MixinFloatTag implements StationNbtFloat {

    @Shadow public float data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtFloat tag && data == tag.value);
    }

    @Override
    public NbtFloat copy() {
        return new NbtFloat(data);
    }
}
