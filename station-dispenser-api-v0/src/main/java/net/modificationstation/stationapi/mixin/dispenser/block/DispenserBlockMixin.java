package net.modificationstation.stationapi.mixin.dispenser.block;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.CustomDispenseBehavior;
import net.modificationstation.stationapi.api.item.DispenseUtil;
import net.modificationstation.stationapi.impl.dispenser.DispenserInfoStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
    @Unique
    DispenserBlockEntity dispenserBlockEntity;
    @Unique
    ItemStack currentItemStack;

    @Inject(method = "method_1774", at = @At("HEAD"), cancellable = true)
    private void dispense(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        dispenserBlockEntity = (DispenserBlockEntity) world.method_1777(x, y, z);
        currentItemStack = dispenserBlockEntity.method_665();
        int slot = DispenserInfoStorage.slot;
        ItemStack[] inventory = DispenserInfoStorage.inventory;

        // non-invasive restore
        if (inventory != null) {
            if (inventory[slot] == null) {
                inventory[slot] = currentItemStack;
            } else {
                inventory[slot].count++;
            }
        }


        if (currentItemStack != null) {
            if (currentItemStack.getItem() instanceof CustomDispenseBehavior behavior) {
                behavior.dispense(new DispenseUtil(world, currentItemStack, dispenserBlockEntity, inventory, slot));
                ci.cancel();
            }
        }
    }

    @Redirect(method = "method_1774", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1777(III)Lnet/minecraft/block/entity/BlockEntity;"))
    BlockEntity giveBlockEntity(World instance, int j, int k, int i) {
        return dispenserBlockEntity;
    }

    @Redirect(method = "method_1774", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/DispenserBlockEntity;method_665()Lnet/minecraft/item/ItemStack;"))
    ItemStack giveItemStack(DispenserBlockEntity instance) {
        return currentItemStack;
    }
}