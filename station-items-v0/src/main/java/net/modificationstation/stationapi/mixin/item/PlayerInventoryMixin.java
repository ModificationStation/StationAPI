package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(PlayerInventory.class)
class PlayerInventoryMixin {
    @Shadow
    public ItemStack[] main;

    @Inject(
            method = "combineStacks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;<init>(III)V",
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_newItemStack(ItemStack par1, CallbackInfoReturnable<Integer> cir, int var2, int var3, int var4) {
        StationNBTSetter.cast(main[var4]).setStationNbt(par1.getStationNbt());
    }

    @Redirect(
            method = "indexOf(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private int stationapi_captureItemStack(ItemStack instance, ItemStack instance2) {
        if (Objects.equals(instance.getStationNbt(), instance2.getStationNbt()))
            return instance.count;
        else {
            notchGodDamnit = true;
            return Integer.MAX_VALUE;
        }
    }

    @Unique
    private boolean notchGodDamnit;

    @Redirect(
            method = "indexOf(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;getMaxCountPerStack()I"
            )
    )
    private int stationapi_fixStackableNBTs(PlayerInventory instance) {
        if (notchGodDamnit) {
            notchGodDamnit = false;
            return Integer.MIN_VALUE;
        } else
            return instance.getMaxCountPerStack();
    }
}
