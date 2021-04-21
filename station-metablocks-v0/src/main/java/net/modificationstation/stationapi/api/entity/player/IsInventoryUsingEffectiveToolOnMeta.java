package net.modificationstation.stationapi.api.entity.player;

import net.minecraft.block.BlockBase;

public interface IsInventoryUsingEffectiveToolOnMeta {

    boolean isUsingEffectiveTool(BlockBase block, int meta);
}
