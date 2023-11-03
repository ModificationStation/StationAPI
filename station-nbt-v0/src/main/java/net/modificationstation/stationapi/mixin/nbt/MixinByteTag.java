package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtByte;
import net.modificationstation.stationapi.api.nbt.StationNbtByte;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtByte.class)
public class MixinByteTag implements StationNbtByte {

    @Shadow public byte data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtByte tag && data == tag.value);
    }

    @Override
    public NbtByte copy() {
        return new NbtByte(data);
    }
}
