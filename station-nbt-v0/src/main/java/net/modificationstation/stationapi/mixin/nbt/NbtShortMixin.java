package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtShort;
import net.modificationstation.stationapi.api.nbt.StationNbtShort;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtShort.class)
class NbtShortMixin implements StationNbtShort {
    @Shadow public short value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtShort tag && value == tag.value);
    }

    @Override
    @Unique
    public NbtShort copy() {
        return new NbtShort(value);
    }
}
