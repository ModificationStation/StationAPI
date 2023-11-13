package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolItem;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolItem.class)
class ToolItemMixin implements StationToolItem {
    @Shadow protected ToolMaterial toolMaterial;
    @Unique
    private TagKey<Block> stationapi_effectiveBlocks;

    @Override
    public void setEffectiveBlocks(TagKey<Block> effectiveBlocks) {
        stationapi_effectiveBlocks = effectiveBlocks;
    }

    @Override
    public TagKey<Block> getEffectiveBlocks(ItemStack stack) {
        return stationapi_effectiveBlocks;
    }

    @Override
    public ToolMaterial getMaterial(ItemStack stack) {
        return toolMaterial;
    }
}
