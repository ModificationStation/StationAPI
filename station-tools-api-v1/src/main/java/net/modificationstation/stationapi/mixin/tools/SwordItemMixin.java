package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwordItem.class)
class SwordItemMixin implements StationSwordItem {
    @Unique
    private ToolMaterial stationapi_toolMaterial;
    @Unique
    private TagKey<Block> stationapi_effectiveBlocks;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stationapi_captureToolMaterial(int i, ToolMaterial arg, CallbackInfo ci) {
        stationapi_toolMaterial = arg;
        setEffectiveBlocks(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/sword")));
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
}
