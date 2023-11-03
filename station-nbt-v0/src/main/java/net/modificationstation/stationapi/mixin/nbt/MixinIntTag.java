package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtInt;
import net.modificationstation.stationapi.api.nbt.StationNbtInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtInt.class)
public class MixinIntTag implements StationNbtInt {

    @Shadow public int data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtInt tag && data == tag.value);
    }

    @Override
    public NbtInt copy() {
        return new NbtInt(data);
    }
}
