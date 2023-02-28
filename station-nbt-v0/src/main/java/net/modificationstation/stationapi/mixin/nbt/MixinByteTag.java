package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.ByteTag;
import net.modificationstation.stationapi.api.nbt.StationNbtByte;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ByteTag.class)
public class MixinByteTag implements StationNbtByte {

    @Shadow public byte data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof ByteTag tag && data == tag.data);
    }

    @Override
    public ByteTag copy() {
        return new ByteTag(data);
    }
}
