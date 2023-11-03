package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtByteArray;
import net.modificationstation.stationapi.api.nbt.StationNbtByteArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;

@Mixin(NbtByteArray.class)
public class MixinByteArrayTag implements StationNbtByteArray {

    @Shadow public byte[] data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtByteArray tag && Arrays.equals(data, tag.value));
    }

    @Override
    public NbtByteArray copy() {
        return new NbtByteArray(Arrays.copyOf(data, data.length));
    }
}
