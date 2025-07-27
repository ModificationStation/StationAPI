package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(ScreenHandler.class)
class ScreenHandlerMixin {
    @Inject(
            method = "onSlotClick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_captureSecondItemStack(int clickType, int flag, boolean player, PlayerEntity par4, CallbackInfoReturnable<ItemStack> cir, ItemStack var5, PlayerInventory var6, Slot var12, ItemStack var13, ItemStack var14) {
        otherStationNBT = var14.getStationNbt();
    }

    @Unique
    private NbtCompound otherStationNBT;

    @Redirect(
            method = "onSlotClick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private int stationapi_continueStatement(ItemStack instance) {
        if (Objects.equals(instance.getStationNbt(), otherStationNBT))
            return instance.itemId;
        else {
            notchGodDamnit = true;
            return Integer.MIN_VALUE;
        }
    }

    @Unique
    private boolean notchGodDamnit;

    @Redirect(
            method = "onSlotClick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private int stationapi_fixStackableNBTs(ItemStack instance) {
        if (notchGodDamnit) {
            notchGodDamnit = false;
            return Integer.MAX_VALUE;
        } else
            return instance.itemId;
    }

    @Inject(
            method = "onSlotClick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_captureFirstItemStack(int clickType, int flag, boolean player, PlayerEntity par4, CallbackInfoReturnable<ItemStack> cir, ItemStack var5, PlayerInventory var6, Slot var12, ItemStack var13) {
        thisStationNBT = var13.getStationNbt();
    }

    @Unique
    private NbtCompound thisStationNBT;

    @Redirect(
            method = "onSlotClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getMaxCount()I",
                    ordinal = 2
            )
    )
    private int stationapi_cancelStatement(ItemStack instance) {
        return Objects.equals(thisStationNBT, instance.getStationNbt()) ? instance.itemId : 0;
    }

    @Redirect(
            method = "insertItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private int stationapi_checkStatement(ItemStack instance, ItemStack arg, int i, int j, boolean flag) {
        if (Objects.equals(instance.getStationNbt(), arg.getStationNbt()))
            return instance.itemId;
        else
            return arg.itemId - 1;
    }
}
