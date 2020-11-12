package net.modificationstation.stationloader.api.common.item;

import net.minecraft.block.BlockBase;

public interface EffectiveOnMeta {

    boolean isEffectiveOn(BlockBase tile, int meta);
}
