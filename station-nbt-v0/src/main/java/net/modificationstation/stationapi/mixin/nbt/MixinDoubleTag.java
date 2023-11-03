package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtDouble;
import net.modificationstation.stationapi.api.nbt.StationNbtDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtDouble.class)
public class MixinDoubleTag implements StationNbtDouble {

    @Shadow public double data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtDouble tag && data == tag.value);
    }

    @Override
    public NbtDouble copy() {
        return new NbtDouble(data);
    }
}
