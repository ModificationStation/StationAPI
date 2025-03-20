package net.modificationstation.stationapi.mixin.tools;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolItem;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;
import net.modificationstation.stationapi.impl.item.ToolEffectivenessImpl;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToolItem.class)
class ToolItemMixin extends Item implements StationToolItem {
    @Shadow protected ToolMaterial toolMaterial;
    @Unique
    private TagKey<Block> stationapi_effectiveBlocks;

    @Unique
    private AbstractToolMaterial stationapi_abstractToolMaterial;

    private ToolItemMixin(int id) {
        super(id);
    }

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ToolItem;toolMaterial:Lnet/minecraft/item/ToolMaterial;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_assignAbstractToolMaterial(ToolItem instance, ToolMaterial value, Operation<Void> original) {
        stationapi_abstractToolMaterial = AbstractToolMaterialImpl.popAbstractToolMaterial();
        if (value != null && stationapi_abstractToolMaterial != null)
            throw new IllegalStateException("Received both an AbstractToolMaterial and a vanilla ToolMaterial! Only one is allowed.");
        else if (value != null)
            stationapi_abstractToolMaterial = value;
        else if (stationapi_abstractToolMaterial != null && stationapi_abstractToolMaterial instanceof ToolMaterial material)
            value = material;
        original.call(instance, value);
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

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ToolMaterial;getMiningSpeedMultiplier()F"
            )
    )
    private float stationapi_interceptGetMiningSpeedMultiplier(ToolMaterial instance, Operation<Float> original) {
        return instance == null ? stationapi_abstractToolMaterial.miningSpeed() : original.call(instance);
    }

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ToolMaterial;getAttackDamage()I"
            )
    )
    private int stationapi_interceptGetAttackDamage(ToolMaterial instance, Operation<Integer> original) {
        return instance == null ? stationapi_abstractToolMaterial.attackDamage() : original.call(instance);
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
