package net.modificationstation.stationapi.mixin.dispenser.block.entity;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.item.CustomDispenseBehavior;
import net.modificationstation.stationapi.impl.dispenser.DispenserInfoStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlockEntity.class)
public class DispenserBlockEntityMixin {
    @Shadow
    private ItemStack[] inventory;

    @Inject(method = "removeStack", at = @At("HEAD"))
    public void customRemoveStack(int slot, int amt, CallbackInfoReturnable<ItemStack> cir) {
        if (inventory[slot] != null) {
            if (inventory[slot].getItem() instanceof CustomDispenseBehavior) {
                DispenserInfoStorage.slot = slot;
                DispenserInfoStorage.inventory = inventory;
            }
        }
    }
}
