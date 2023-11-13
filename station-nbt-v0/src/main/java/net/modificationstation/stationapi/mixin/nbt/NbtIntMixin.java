package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtInt;
import net.modificationstation.stationapi.api.nbt.StationNbtInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtInt.class)
class NbtIntMixin implements StationNbtInt {
    @Shadow public int value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtInt tag && value == tag.value);
    }

    @Override
    @Unique
    public NbtInt copy() {
        return new NbtInt(value);
    }
}
