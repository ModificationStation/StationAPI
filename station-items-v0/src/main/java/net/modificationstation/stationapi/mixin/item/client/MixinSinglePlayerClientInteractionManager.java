package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.SingleplayerInteractionManager;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
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
public abstract class MixinSinglePlayerClientInteractionManager extends InteractionManager {

    public MixinSinglePlayerClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "getBlockReachDistance()F", at = @At("RETURN"), cancellable = true)
    private void getBlockReach(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(minecraft.player)
                        .type(HitResultType.BLOCK)
                        .currentReach(cir.getReturnValueF())
                        .build()
        ).currentReach);
    }

    @Inject(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"), cancellable = true)
    public void method_1721_preMine(int x, int y, int z, int side, CallbackInfo ci){
        ItemStack itemInstance = this.minecraft.player.inventory.getSelectedItem();
        if(itemInstance != null){
            if(!itemInstance.preMine(this.minecraft.player.world.getBlockState(x, y, z), x, y, z, side, this.minecraft.player)){
                ci.cancel();
            }
        }
    }
}
