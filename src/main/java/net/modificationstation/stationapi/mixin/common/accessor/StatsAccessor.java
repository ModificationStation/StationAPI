package net.modificationstation.stationapi.mixin.common.accessor;

import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Stats.class)
public interface StatsAccessor {

    @Accessor
    static void setField_812(boolean field_812) {
        throw new UnsupportedOperationException("Mixin!");
    }

    @Accessor
    static void setField_813(boolean field_813) {
        throw new UnsupportedOperationException("Mixin!");
    }
}
