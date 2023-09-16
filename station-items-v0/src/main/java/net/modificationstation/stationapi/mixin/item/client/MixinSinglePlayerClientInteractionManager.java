package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SinglePlayerClientInteractionManager;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePlayerClientInteractionManager.class)
public class MixinSinglePlayerClientInteractionManager extends BaseClientInteractionManager {

    public MixinSinglePlayerClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "getBlockReachDistance()F", at = @At("RETURN"), cancellable = true)
    private void getBlockReach(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(minecraft.player)
                        .type(HitType.field_789)
                        .currentReach(cir.getReturnValueF())
                        .build()
        ).currentReach);
    }

    @Inject(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"), cancellable = true)
    public void method_1721_preMine(int x, int y, int z, int par4, CallbackInfo ci){
        ItemInstance itemInstance = this.minecraft.player.inventory.getHeldItem();
        if(itemInstance != null){
            if(!itemInstance.preMine(x,y,z,par4,this.minecraft.player)){
                ci.cancel();
            }
        }
    }
}
