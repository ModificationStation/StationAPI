package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.nbt.StationNbtList;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Objects;

@Mixin(NbtList.class)
public class MixinListTag implements StationNbtList {

    @Shadow private List<NbtElement> data;

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtList tag && Objects.equals(data, ((ListTagAccessor) tag).stationapi$getData()));
    }

    @Override
    public NbtList copy() {
        return Util.make(new NbtList(), tag -> data.forEach(value -> tag.add(value.copy())));
    }
}
