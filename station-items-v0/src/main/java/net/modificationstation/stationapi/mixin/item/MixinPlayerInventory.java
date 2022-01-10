package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.item.nbt.HasItemEntity;
import net.modificationstation.stationapi.api.item.nbt.ItemEntity;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.nbt.NBTHelper;
import net.modificationstation.stationapi.impl.item.nbt.StationNBTSetter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {

    @Shadow
    public ItemInstance[] main;

    @Deprecated
    @Inject(method = "mergeStacks(Lnet/minecraft/item/ItemInstance;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;<init>(III)V", shift = At.Shift.BY, by = 2), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onCopyItemInstance(ItemInstance arg, CallbackInfoReturnable<Integer> cir, int var2, int var3, int var4) {
        ItemEntity itemEntity = HasItemEntity.cast(arg).getItemEntity();
        HasItemEntity.cast(main[var4]).setItemEntity(itemEntity == null ? null : itemEntity.copy());
    }

    @Inject(
            method = "mergeStacks(Lnet/minecraft/item/ItemInstance;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemInstance;<init>(III)V",
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void newItemInstance(ItemInstance par1, CallbackInfoReturnable<Integer> cir, int var2, int var3, int var4) {
        StationNBTSetter.cast(main[var4]).setStationNBT(StationNBT.cast(par1).getStationNBT());
    }

    @Redirect(
            method = "getIdenticalStackSlot(Lnet/minecraft/item/ItemInstance;)I",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private int captureItemInstance(ItemInstance instance, ItemInstance instance2) {
        if (NBTHelper.equals(StationNBT.cast(instance).getStationNBT(), StationNBT.cast(instance2).getStationNBT()))
            return instance.count;
        else {
            notchGodDamnit = true;
            return Integer.MAX_VALUE;
        }
    }

    @Unique
    private boolean notchGodDamnit;

    @Redirect(
            method = "getIdenticalStackSlot(Lnet/minecraft/item/ItemInstance;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;getMaxItemCount()I"
            )
    )
    private int fixStackableNBTs(PlayerInventory instance) {
        if (notchGodDamnit) {
            notchGodDamnit = false;
            return Integer.MIN_VALUE;
        } else
            return instance.getMaxItemCount();
    }
}
