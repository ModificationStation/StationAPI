package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.LongArrayTag;
import net.modificationstation.stationapi.impl.nbt.LongArrayCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

@Mixin(CompoundTag.class)
public class MixinCompoundTag implements LongArrayCompound {

    @Shadow private Map<String, AbstractTag> data;

    @Override
    @Unique
    public void put(String key, long[] item) {
        data.put(key, new LongArrayTag(item).setType(key));
    }

    @Override
    @Unique
    public long[] getLongArray(String key) {
        return !this.data.containsKey(key) ? new long[0] : ((LongArrayTag) this.data.get(key)).data;
    }
}
