package net.modificationstation.stationapi.mixin.sortme.common.accessor;

import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Stats.class)
public interface StatsAccessor {

    @Accessor
    static void setBlocksInit(boolean blocksInit) {
        throw new UnsupportedOperationException("Mixin!");
    }

    @Accessor
    static void setItemsInit(boolean itemsInit) {
        throw new UnsupportedOperationException("Mixin!");
    }
}
