package net.modificationstation.stationloader.api.common.block;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.item.tool.ToolLevel;

import java.util.List;

public interface BlockMiningLevel {
    int getBlockLevel(int meta, ItemInstance itemInstance);

    List<Class<? extends ToolLevel>> getToolTypes(int meta, ItemInstance itemInstance);
}
