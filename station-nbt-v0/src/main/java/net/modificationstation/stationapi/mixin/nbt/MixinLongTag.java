package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtLong;
import net.modificationstation.stationapi.api.nbt.StationNbtLong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtLong.class)
public class MixinLongTag implements StationNbtLong {

    @Shadow public long data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtLong tag && data == tag.value);
    }

    @Override
    public NbtLong copy() {
        return new NbtLong(data);
    }
}
