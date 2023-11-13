package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface StationToolItem extends ToolLevel {

    @Override
    default void setEffectiveBlocks(TagKey<Block> effectiveBlocks) {
        Util.assertImpl();
    }

    @Override
    default TagKey<Block> getEffectiveBlocks(ItemStack stack) {
        return Util.assertImpl();
    }

    @Override
    default ToolMaterial getMaterial(ItemStack stack) {
        return Util.assertImpl();
    }
}
