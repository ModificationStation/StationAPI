package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.StringTag;
import net.modificationstation.stationapi.api.nbt.StationNbtString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(StringTag.class)
public class MixinStringTag implements StationNbtString {

    @Shadow public String data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof StringTag tag && Objects.equals(data, tag.data));
    }

    @Override
    public StringTag copy() {
        return new StringTag(data);
    }
}
