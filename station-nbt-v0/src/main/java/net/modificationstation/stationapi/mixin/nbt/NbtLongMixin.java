package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtLong;
import net.modificationstation.stationapi.api.nbt.StationNbtLong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtLong.class)
class NbtLongMixin implements StationNbtLong {
    @Shadow public long value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtLong tag && value == tag.value);
    }

    @Override
    @Unique
    public NbtLong copy() {
        return new NbtLong(value);
    }
}
