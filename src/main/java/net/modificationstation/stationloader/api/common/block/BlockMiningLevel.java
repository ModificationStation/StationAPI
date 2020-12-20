package net.modificationstation.stationloader.api.common.block;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.item.tool.ToolLevel;

public interface BlockMiningLevel {
    int getBlockLevel(int meta, ItemInstance itemInstance);

    Class<? extends ToolLevel> getToolType(int meta, ItemInstance itemInstance);
}
