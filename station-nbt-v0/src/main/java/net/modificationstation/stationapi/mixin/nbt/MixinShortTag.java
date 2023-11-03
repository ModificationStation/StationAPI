package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtShort;
import net.modificationstation.stationapi.api.nbt.StationNbtShort;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtShort.class)
public class MixinShortTag implements StationNbtShort {

    @Shadow public short data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtShort tag && data == tag.value);
    }

    @Override
    public NbtShort copy() {
        return new NbtShort(data);
    }
}
