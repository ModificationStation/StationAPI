package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.ByteArrayTag;
import net.modificationstation.stationapi.api.nbt.StationNbtByteArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;

@Mixin(ByteArrayTag.class)
public class MixinByteArrayTag implements StationNbtByteArray {

    @Shadow public byte[] data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof ByteArrayTag tag && Arrays.equals(data, tag.data));
    }

    @Override
    public ByteArrayTag copy() {
        return new ByteArrayTag(Arrays.copyOf(data, data.length));
    }
}
