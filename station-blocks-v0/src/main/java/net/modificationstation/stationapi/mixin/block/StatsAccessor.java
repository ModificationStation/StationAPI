package net.modificationstation.stationapi.mixin.block;

import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.*;

@Mixin(Stats.class)
public interface StatsAccessor {

    @Accessor
    static Map<Integer, Stat> getIdMap() {
        return Util.assertMixin();
    }
}
