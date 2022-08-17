package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.tag.TagKey;

public interface ToolLevel {

    void setEffectiveBlocks(TagKey<BlockBase> effectiveBlocks);

    TagKey<BlockBase> getEffectiveBlocks(ItemInstance itemInstance);

    ToolMaterial getMaterial(ItemInstance itemInstance);
}
