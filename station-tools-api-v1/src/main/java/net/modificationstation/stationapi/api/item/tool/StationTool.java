package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.tag.TagKey;

/**
 * This interface is injected into all tool items and "pseudo" tool items (such as {@link StationHoeItem},
 * {@link StationShearsItem} and {@link StationSwordItem}) to provide the StationAPI's extended tools functionality.
 */
public interface StationTool {
    void setEffectiveBlocks(TagKey<Block> effectiveBlocks);

    TagKey<Block> getEffectiveBlocks(ItemStack stack);

    ToolMaterial getMaterial(ItemStack stack);
}
