package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtString;
import net.modificationstation.stationapi.api.nbt.StationNbtString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(NbtString.class)
class NbtStringMixin implements StationNbtString {
    @Shadow public String value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtString tag && Objects.equals(value, tag.value));
    }

    @Override
    @Unique
    public NbtString copy() {
        return new NbtString(value);
    }
}
