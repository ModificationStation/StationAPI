package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtDouble;
import net.modificationstation.stationapi.api.nbt.StationNbtDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtDouble.class)
class NbtDoubleMixin implements StationNbtDouble {
    @Shadow public double value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtDouble tag && value == tag.value);
    }

    @Override
    @Unique
    public NbtDouble copy() {
        return new NbtDouble(value);
    }
}
