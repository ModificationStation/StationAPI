package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.nbt.StationNbtList;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Objects;

@Mixin(ListTag.class)
public class MixinListTag implements StationNbtList {

    @Shadow private List<AbstractTag> data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof ListTag tag && Objects.equals(data, ((ListTagAccessor) tag).stationapi$getData()));
    }

    @Override
    public ListTag copy() {
        return Util.make(new ListTag(), tag -> data.forEach(value -> tag.add(value.copy())));
    }
}
