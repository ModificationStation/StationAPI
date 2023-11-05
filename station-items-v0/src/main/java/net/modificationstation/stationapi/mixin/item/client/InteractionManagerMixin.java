package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.InteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.UseOnBlockFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InteractionManager.class)
class InteractionManagerMixin {
    @Inject(
            method = "method_1713",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_injectOnPlaceBlock(PlayerEntity playerBase, World world, ItemStack stack, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        if (stack != null && stack.getItem() instanceof UseOnBlockFirst use && use.onUseOnBlockFirst(stack, playerBase, world, i, j, k, i1))
            cir.setReturnValue(true);
    }
}
