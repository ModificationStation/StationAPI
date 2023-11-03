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
public class MixinClientInteractionManager {

    @Inject(method = "method_1713(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;Lnet/minecraft/item/ItemInstance;IIII)Z", at = @At("HEAD"), cancellable = true)
    private void injectOnPlaceBlock(PlayerEntity playerBase, World level, ItemStack itemInstance, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        if (itemInstance != null && itemInstance.getItem() instanceof UseOnBlockFirst use && use.onUseOnBlockFirst(itemInstance, playerBase, level, i, j, k, i1))
            cir.setReturnValue(true);
    }
}
