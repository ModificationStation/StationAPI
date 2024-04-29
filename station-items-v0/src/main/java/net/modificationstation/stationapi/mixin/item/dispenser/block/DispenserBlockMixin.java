package net.modificationstation.stationapi.mixin.item.dispenser.block;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dispenser.DispenseEvent;
import net.modificationstation.stationapi.api.dispenser.ItemDispenseContext;
import net.modificationstation.stationapi.impl.dispenser.DispenserInfoStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(DispenserBlock.class)
class DispenserBlockMixin {
    @Unique
    private DispenserBlockEntity dispenserBlockEntity;
    @Unique
    private ItemStack currentItemStack;

    @Inject(method = "method_1774", at = @At("HEAD"), cancellable = true)
    private void stationapi_customDispense(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        dispenserBlockEntity = (DispenserBlockEntity) world.method_1777(x, y, z);
        currentItemStack = dispenserBlockEntity.method_665();
        int slot = DispenserInfoStorage.slot;
        ItemStack[] inventory = DispenserInfoStorage.inventory;

        ItemDispenseContext context = new ItemDispenseContext(world, currentItemStack, dispenserBlockEntity, inventory, slot);

        if (StationAPI.EVENT_BUS.post(
                DispenseEvent.builder().context(context).build()
        ).isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "method_1774", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1777(III)Lnet/minecraft/block/entity/BlockEntity;"))
    private BlockEntity stationapi_provideBlockEntity(World instance, int j, int k, int i) {
        return dispenserBlockEntity;
    }

    @Redirect(method = "method_1774", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/DispenserBlockEntity;method_665()Lnet/minecraft/item/ItemStack;"))
    private ItemStack stationapi_provideItemStack(DispenserBlockEntity instance) {
        return currentItemStack;
    }
}
