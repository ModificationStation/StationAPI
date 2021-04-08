package net.modificationstation.stationapi.api.block;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.item.tool.ToolLevel;

import java.util.*;

public interface BlockMiningLevel {
    int getBlockLevel(int meta, ItemInstance itemInstance);

    List<Class<? extends ToolLevel>> getToolTypes(int meta, ItemInstance itemInstance);
}
