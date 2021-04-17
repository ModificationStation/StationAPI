package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.BlockBase;

public interface OverrideIsEffectiveOn {

    boolean overrideIsEffectiveOn(ToolLevel toolLevel, BlockBase block, int meta, boolean effective);
}
