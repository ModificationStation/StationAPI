package net.modificationstation.stationapi.mixin.tools;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationHoeItem;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;
import net.modificationstation.stationapi.impl.item.ToolEffectivenessImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoeItem.class)
class HoeItemMixin extends Item implements StationHoeItem {
    @Unique
    private AbstractToolMaterial stationapi_abstractToolMaterial;
    @Unique
    private TagKey<Block> stationapi_effectiveBlocks;

    protected HoeItemMixin(int id) {
        super(id);
    }

    @Inject(
            method = "<init>",
            at = @At("CTOR_HEAD")
    )
    private void stationapi_captureToolMaterial(int i, ToolMaterial arg, CallbackInfo ci) {
        stationapi_abstractToolMaterial = AbstractToolMaterialImpl.popAbstractToolMaterial();
        if (arg != null) {
            if (stationapi_abstractToolMaterial != null) throw new IllegalStateException(
                    "Received both an AbstractToolMaterial and a vanilla ToolMaterial! Only one is allowed."
            );
            stationapi_abstractToolMaterial = arg;
        }
        setEffectiveBlocks(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/hoe")));
    }

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ToolMaterial;getDurability()I"
            )
    )
    private int stationapi_interceptGetDurability(ToolMaterial instance, Operation<Integer> original) {
        return instance == null ? stationapi_abstractToolMaterial.itemDurability() : original.call(instance);
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
    public AbstractToolMaterial getMaterial(ItemStack stack) {
        return stationapi_abstractToolMaterial;
    }

    @Override
    public boolean isSuitableFor(PlayerEntity player, ItemStack itemStack, BlockView blockView, BlockPos blockPos, BlockState state) {
        return ToolEffectivenessImpl.shouldApplyCustomLogic(itemStack, state)
                ? ToolEffectivenessImpl.isSuitableFor(itemStack, state) : super.isSuitableFor(player, itemStack, blockView, blockPos, state);
    }

    @Override
    public float getMiningSpeedMultiplier(PlayerEntity player, ItemStack itemStack, BlockView blockView, BlockPos blockPos, BlockState state) {
        return ToolEffectivenessImpl.shouldApplyCustomLogic(itemStack, state) && ToolEffectivenessImpl.isSuitableFor(itemStack, state)
                ? ToolEffectivenessImpl.getMiningSpeedMultiplier(itemStack) : super.getMiningSpeedMultiplier(player, itemStack, blockView, blockPos, state);
    }
}
