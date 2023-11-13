package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.api.nbt.NbtIntArray;
import net.modificationstation.stationapi.api.nbt.NbtLongArray;
import net.modificationstation.stationapi.api.nbt.StationNbtCompound;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Objects;

@Mixin(NbtCompound.class)
class NbtCompoundMixin implements StationNbtCompound {
    @Shadow private Map<String, NbtElement> entries;

    @Override
    @Unique
    public void put(String key, int[] item) {
        entries.put(key, new NbtIntArray(item).setKey(key));
    }

    @Override
    @Unique
    public int[] getIntArray(String key) {
        return !this.entries.containsKey(key) ? new int[0] : ((NbtIntArray) this.entries.get(key)).data;
    }

    @Override
    @Unique
    public void put(String key, long[] item) {
        entries.put(key, new NbtLongArray(item).setKey(key));
    }

    @Override
    @Unique
    public long[] getLongArray(String key) {
        return !this.entries.containsKey(key) ? new long[0] : ((NbtLongArray) this.entries.get(key)).data;
    }

    @Override
    @Unique
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtCompound tag && Objects.equals(entries, ((NbtCompoundAccessor) tag).stationapi$getEntries()));
    }

    @Override
    @Unique
    public NbtCompound copy() {
        return Util.make(new NbtCompound(), tag -> entries.forEach((key, value) -> tag.put(key, value.copy())));
    }
}
