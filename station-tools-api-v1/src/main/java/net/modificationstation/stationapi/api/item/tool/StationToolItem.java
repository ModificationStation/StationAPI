package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface StationToolItem extends StationTool {
    @Override
    default void setEffectiveBlocks(TagKey<Block> effectiveBlocks) {
        Util.assertImpl();
    }

    @Override
    default TagKey<Block> getEffectiveBlocks(ItemStack stack) {
        return Util.assertImpl();
    }

    @Override
    default AbstractToolMaterial getMaterial(ItemStack stack) {
        return Util.assertImpl();
    }
}
