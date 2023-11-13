package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtByte;
import net.modificationstation.stationapi.api.nbt.StationNbtByte;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtByte.class)
class NbtByteMixin implements StationNbtByte {
    @Shadow public byte value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtByte tag && value == tag.value);
    }

    @Override
    @Unique
    public NbtByte copy() {
        return new NbtByte(value);
    }
}
