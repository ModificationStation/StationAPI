package net.modificationstation.stationapi.api.common.item.tool;

import net.minecraft.block.BlockBase;

public interface OverrideIsEffectiveOn {

    boolean overrideIsEffectiveOn(ToolLevel toolLevel, BlockBase block, int meta, boolean effective);
}
