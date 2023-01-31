package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.nbt.NbtIntArray;
import net.modificationstation.stationapi.api.nbt.NbtLongArray;
import net.modificationstation.stationapi.api.nbt.StationNbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(CompoundTag.class)
public class MixinCompoundTag implements StationNbtCompound {

    @Shadow private Map<String, AbstractTag> data;

    @Override
    @Unique
    public void put(String key, int[] item) {
        data.put(key, new NbtIntArray(item).setType(key));
    }

    @Override
    @Unique
    public int[] getIntArray(String key) {
        return !this.data.containsKey(key) ? new int[0] : ((NbtIntArray) this.data.get(key)).data;
    }

    @Override
    @Unique
    public void put(String key, long[] item) {
        data.put(key, new NbtLongArray(item).setType(key));
    }

    @Override
    @Unique
    public long[] getLongArray(String key) {
        return !this.data.containsKey(key) ? new long[0] : ((NbtLongArray) this.data.get(key)).data;
    }
}
