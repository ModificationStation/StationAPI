package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtEnd;
import net.modificationstation.stationapi.api.nbt.StationNbtEnd;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtEnd.class)
class NbtEndMixin implements StationNbtEnd {
    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || obj instanceof NbtEnd;
    }

    @Override
    @Unique
    public NbtEnd copy() {
        return new NbtEnd();
    }
}
