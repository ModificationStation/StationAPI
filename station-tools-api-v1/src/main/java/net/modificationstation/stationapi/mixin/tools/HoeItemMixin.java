package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.Block;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.tool.StationHoeItem;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.ToolEffectivenessImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoeItem.class)
class HoeItemMixin extends Item implements StationHoeItem {
    @Unique
    private ToolMaterial stationapi_toolMaterial;
    @Unique
    private TagKey<Block> stationapi_effectiveBlocks;

    protected HoeItemMixin(int id) {
        super(id);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stationapi_captureToolMaterial(int i, ToolMaterial arg, CallbackInfo ci) {
        stationapi_toolMaterial = arg;
        setEffectiveBlocks(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/hoe")));
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
        return stationapi_toolMaterial;
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
