package net.modificationstation.stationapi.mixin.item.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.UseOnBlockFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
class class_70Mixin {
    @Shadow public PlayerEntity player;

    @Inject(
            method = "interactBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_injectOnPlaceBlock(PlayerEntity playerBase, World world, ItemStack stack, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        if (stack != null && stack.getItem() instanceof UseOnBlockFirst use && use.onUseOnBlockFirst(stack, playerBase, world, i, j, k, i1))
            cir.setReturnValue(true);
    }

    @Inject(
            method = "onBlockBreakingAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_73;getBlockId(III)I"
            ),
            cancellable = true
    )
    public void stationapi_onBlockBreakingAction_preMine(int x, int y, int z, int side, CallbackInfo ci){
        ItemStack stack = this.player.inventory.getSelectedItem();
        if (stack != null && !stack.preMine(this.player.world.getBlockState(x, y, z), x, y, z, side, this.player))
            ci.cancel();
    }
}
