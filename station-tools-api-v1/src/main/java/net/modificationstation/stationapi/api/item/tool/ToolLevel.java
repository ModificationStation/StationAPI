package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;

public interface ToolLevel {

    ToolMaterial getMaterial(ItemInstance itemInstance);
}
