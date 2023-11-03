package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtEnd;
import net.modificationstation.stationapi.api.nbt.StationNbtEnd;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NbtEnd.class)
public class MixinEndTag implements StationNbtEnd {

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof NbtEnd;
    }

    @Override
    public NbtEnd copy() {
        return new NbtEnd();
    }
}
