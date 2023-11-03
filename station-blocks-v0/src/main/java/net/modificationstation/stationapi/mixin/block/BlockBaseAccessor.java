package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockBaseAccessor {

    @Mutable
    @Accessor("BY_ID")
    static void stationapi$setById(Block[] byId) {
        Util.assertMixin();
    }

    @Mutable
    @Accessor("TICKS_RANDOMLY")
    static void stationapi$setTicksRandomly(boolean[] ticksRandomly) {
        Util.assertMixin();
    }

    @Mutable
    @Accessor("FULL_OPAQUE")
    static void stationapi$setFullOpaque(boolean[] fullOpaque) {
        Util.assertMixin();
    }

    @Mutable
    @Accessor("HAS_TILE_ENTITY")
    static void stationapi$setHasTileEntity(boolean[] hasTileEntity) {
        Util.assertMixin();
    }

    @Mutable
    @Accessor("LIGHT_OPACITY")
    static void stationapi$setLightOpacity(int[] lightOpacity) {
        Util.assertMixin();
    }

    @Mutable
    @Accessor("ALLOWS_GRASS_UNDER")
    static void stationapi$setAllowsGrassUnder(boolean[] allowsGrassUnder) {
        Util.assertMixin();
    }

    @Mutable
    @Accessor("EMITTANCE")
    static void stationapi$setEmittance(int[] emittance) {
        Util.assertMixin();
    }

    @Mutable
    @Accessor("NO_NOTIFY_ON_META_CHANGE")
    static void stationapi$setNoNotifyOnMetaChange(boolean[] noNotifyOnMetaChange) {
        Util.assertMixin();
    }
}
