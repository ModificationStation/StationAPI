package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.IntTag;
import net.modificationstation.stationapi.api.nbt.StationNbtInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IntTag.class)
public class MixinIntTag implements StationNbtInt {

    @Shadow public int data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof IntTag tag && data == tag.data);
    }

    @Override
    public IntTag copy() {
        return new IntTag(data);
    }
}
