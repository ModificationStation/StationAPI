package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtFloat;
import net.modificationstation.stationapi.api.nbt.StationNbtFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtFloat.class)
class NbtFloatMixin implements StationNbtFloat {
    @Shadow public float value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtFloat tag && value == tag.value);
    }

    @Override
    @Unique
    public NbtFloat copy() {
        return new NbtFloat(value);
    }
}
