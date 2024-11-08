package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.tool.StationToolItem;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.impl.item.ToolEffectivenessImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolItem.class)
class ToolItemMixin extends Item implements StationToolItem {
    @Shadow protected ToolMaterial toolMaterial;
    @Unique
    private TagKey<Block> stationapi_effectiveBlocks;

    private ToolItemMixin(int id) {
        super(id);
    }

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

    @Override
    public boolean isSuitableFor(ItemStack itemStack, BlockState state) {
        return ToolEffectivenessImpl.shouldApplyCustomLogic(itemStack, state)
                ? ToolEffectivenessImpl.isSuitableFor(itemStack, state) : super.isSuitableFor(itemStack, state);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack itemStack, BlockState state) {
        return ToolEffectivenessImpl.shouldApplyCustomLogic(itemStack, state) && ToolEffectivenessImpl.isSuitableFor(itemStack, state)
                ? ToolEffectivenessImpl.getMiningSpeedMultiplier(itemStack) : super.getMiningSpeedMultiplier(itemStack, state);
    }
}
