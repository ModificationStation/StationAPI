package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtByteArray;
import net.modificationstation.stationapi.api.nbt.StationNbtByteArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(NbtByteArray.class)
class NbtByteArrayMixin implements StationNbtByteArray {
    @Shadow public byte[] value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtByteArray tag && Arrays.equals(value, tag.value));
    }

    @Override
    @Unique
    public NbtByteArray copy() {
        return new NbtByteArray(Arrays.copyOf(value, value.length));
    }
}
