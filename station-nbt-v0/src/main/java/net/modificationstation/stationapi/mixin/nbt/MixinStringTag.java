package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtString;
import net.modificationstation.stationapi.api.nbt.StationNbtString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(NbtString.class)
public class MixinStringTag implements StationNbtString {

    @Shadow public String data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtString tag && Objects.equals(data, tag.value));
    }

    @Override
    public NbtString copy() {
        return new NbtString(data);
    }
}
