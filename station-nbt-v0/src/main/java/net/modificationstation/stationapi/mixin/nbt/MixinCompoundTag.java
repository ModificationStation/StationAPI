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
public class MixinCompoundTag implements StationNbtCompound {

    @Shadow private Map<String, NbtElement> data;

    @Override
    @Unique
    public void put(String key, int[] item) {
        data.put(key, new NbtIntArray(item).setKey(key));
    }

    @Override
    @Unique
    public int[] getIntArray(String key) {
        return !this.data.containsKey(key) ? new int[0] : ((NbtIntArray) this.data.get(key)).data;
    }

    @Override
    @Unique
    public void put(String key, long[] item) {
        data.put(key, new NbtLongArray(item).setKey(key));
    }

    @Override
    @Unique
    public long[] getLongArray(String key) {
        return !this.data.containsKey(key) ? new long[0] : ((NbtLongArray) this.data.get(key)).data;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtCompound tag && Objects.equals(data, ((CompoundTagAccessor) tag).stationapi$getData()));
    }

    @Override
    public NbtCompound copy() {
        return Util.make(new NbtCompound(), tag -> data.forEach((key, value) -> tag.put(key, value.copy())));
    }
}
