package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.tag.TagKey;

public interface ToolLevel {

    void setEffectiveBlocks(TagKey<Block> effectiveBlocks);

    TagKey<Block> getEffectiveBlocks(ItemStack itemInstance);

    ToolMaterial getMaterial(ItemStack itemInstance);
}
