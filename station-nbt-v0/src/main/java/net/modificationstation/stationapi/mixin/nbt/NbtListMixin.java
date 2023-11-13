package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.nbt.StationNbtList;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Objects;

@Mixin(NbtList.class)
class NbtListMixin implements StationNbtList {
    @Shadow private List<NbtElement> value;

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtList tag && Objects.equals(value, ((NbtListAccessor) tag).stationapi$getValue()));
    }

    @Override
    @Unique
    public NbtList copy() {
        return Util.make(new NbtList(), tag -> value.forEach(value -> tag.add(value.copy())));
    }
}
