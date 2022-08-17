package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface ToolTemplate extends ToolLevel {

    @Override
    default void setEffectiveBlocks(TagKey<BlockBase> effectiveBlocks) {
        Util.assertImpl();
    }

    @Override
    default TagKey<BlockBase> getEffectiveBlocks(ItemInstance itemInstance) {
        return Util.assertImpl();
    }

    @Override
    default ToolMaterial getMaterial(ItemInstance itemInstance) {
        return Util.assertImpl();
    }
}
