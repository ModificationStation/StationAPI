package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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

@Mixin(Container.class)
public class MixinContainerBase {

    @Inject(
            method = "clickSlot(IIZLnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void captureSecondItemInstance(int clickType, int flag, boolean player, PlayerEntity par4, CallbackInfoReturnable<ItemStack> cir, ItemStack var5, PlayerInventory var6, Slot var12, ItemStack var13, ItemStack var14) {
        otherStationNBT = var14.getStationNBT();
    }

    @Unique
    private NbtCompound otherStationNBT;

    @Redirect(
            method = "clickSlot(IIZLnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private int continueStatement(ItemStack instance) {
        if (Objects.equals(instance.getStationNBT(), otherStationNBT))
            return instance.itemId;
        else {
            notchGodDamnit = true;
            return Integer.MIN_VALUE;
        }
    }

    @Unique
    private boolean notchGodDamnit;

    @Redirect(
            method = "clickSlot(IIZLnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private int fixStackableNBTs(ItemStack instance) {
        if (notchGodDamnit) {
            notchGodDamnit = false;
            return Integer.MAX_VALUE;
        } else
            return instance.itemId;
    }

    @Inject(
            method = "clickSlot(IIZLnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void captureFirstItemInstance(int clickType, int flag, boolean player, PlayerEntity par4, CallbackInfoReturnable<ItemStack> cir, ItemStack var5, PlayerInventory var6, Slot var12, ItemStack var13) {
        thisStationNBT = var13.getStationNBT();
    }

    @Unique
    private NbtCompound thisStationNBT;

    @Redirect(
            method = "clickSlot(IIZLnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemInstance;getMaxStackSize()I",
                    ordinal = 2
            )
    )
    private int cancelStatement(ItemStack instance) {
        return Objects.equals(thisStationNBT, instance.getStationNBT()) ? instance.itemId : 0;
    }

    @Redirect(
            method = "insertItem(Lnet/minecraft/item/ItemInstance;IIZ)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private int checkStatement(ItemStack instance, ItemStack arg, int i, int j, boolean flag) {
        if (Objects.equals(instance.getStationNBT(), arg.getStationNBT()))
            return instance.itemId;
        else
            return arg.itemId - 1;
    }
}
