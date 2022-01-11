package net.modificationstation.stationapi.mixin.item;

import net.minecraft.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.nbt.NBTHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ContainerBase.class)
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
    private void captureSecondItemInstance(int clickType, int flag, boolean player, PlayerBase par4, CallbackInfoReturnable<ItemInstance> cir, ItemInstance var5, PlayerInventory var6, Slot var12, ItemInstance var13, ItemInstance var14) {
        otherStationNBT = StationNBT.cast(var14).getStationNBT();
    }

    @Unique
    private CompoundTag otherStationNBT;

    @Redirect(
            method = "clickSlot(IIZLnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private int continueStatement(ItemInstance instance) {
        if (NBTHelper.equals(StationNBT.cast(instance).getStationNBT(), otherStationNBT))
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
    private int fixStackableNBTs(ItemInstance instance) {
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
    private void captureFirstItemInstance(int clickType, int flag, boolean player, PlayerBase par4, CallbackInfoReturnable<ItemInstance> cir, ItemInstance var5, PlayerInventory var6, Slot var12, ItemInstance var13) {
        thisStationNBT = StationNBT.cast(var13).getStationNBT();
    }

    @Unique
    private CompoundTag thisStationNBT;

    @Redirect(
            method = "clickSlot(IIZLnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemInstance;getMaxStackSize()I",
                    ordinal = 2
            )
    )
    private int cancelStatement(ItemInstance instance) {
        return NBTHelper.equals(thisStationNBT, StationNBT.cast(instance).getStationNBT()) ? instance.itemId : 0;
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
    private int checkStatement(ItemInstance instance, ItemInstance arg, int i, int j, boolean flag) {
        if (NBTHelper.equals(StationNBT.cast(instance).getStationNBT(), StationNBT.cast(arg).getStationNBT()))
            return instance.itemId;
        else
            return arg.itemId - 1;
    }
}
