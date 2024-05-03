package net.modificationstation.stationapi.mixin.item.dispenser.block.entity;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.dispenser.DispenserInfoStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlockEntity.class)
class DispenserBlockEntityMixin {
    @Shadow
    private ItemStack[] inventory;

    @Inject(method = "removeStack", at = @At("HEAD"))
    private void stationapi_customDispenserCaptureInventoryAndSlot(int slot, int amt, CallbackInfoReturnable<ItemStack> cir) {
        DispenserInfoStorage.slot = slot;
    }
}
