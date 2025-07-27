package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResultType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleplayerInteractionManager.class)
abstract class SingleplayerInteractionManagerMixin extends InteractionManager {
    private SingleplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(
            method = "method_1715",
            at = @At("RETURN"),
            cancellable = true
    )
    private void stationapi_getBlockReach(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(minecraft.player)
                        .type(HitResultType.BLOCK)
                        .currentReach(cir.getReturnValueF())
                        .build()
        ).currentReach);
    }

    @Inject(
            method = "method_1721",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            ),
            cancellable = true
    )
    private void stationapi_method_1721_preMine(int x, int y, int z, int side, CallbackInfo ci){
        ItemStack stack = this.minecraft.player.inventory.getSelectedItem();
        if (stack != null && !stack.preMine(this.minecraft.player.world.getBlockState(x, y, z), x, y, z, side, this.minecraft.player))
            ci.cancel();
    }
}
