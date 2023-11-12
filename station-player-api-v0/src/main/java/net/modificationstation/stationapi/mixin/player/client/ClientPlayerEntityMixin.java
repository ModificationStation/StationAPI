package net.modificationstation.stationapi.mixin.player.client;

import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.impl.entity.player.PlayerAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
class ClientPlayerEntityMixin {
    @Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
    private void stationapi_onHandleKeyPress(int i, boolean b, CallbackInfo ci) {
        if (PlayerAPI.handleKeyPress((ClientPlayerEntity) (Object) this, i, b))
            ci.cancel();
    }

    @Redirect(method = "method_141", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;method_687()I"))
    private int stationapi_redirectGetPlayerArmorValue(PlayerInventory inventoryPlayer) {
        return PlayerAPI.getPlayerArmorValue((ClientPlayerEntity) (Object) this, inventoryPlayer.method_687());
    }

    @Inject(method = "method_1373", at = @At("RETURN"), cancellable = true)
    private void stationapi_isSneaking(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(PlayerAPI.isSneaking((ClientPlayerEntity) (Object) this, cir.getReturnValue()));
    }

    @Inject(method = "pushOutOfBlock", at = @At("HEAD"), cancellable = true)
    private void stationapi_injectPushOutOfBlocks(double v, double v1, double v2, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerAPI.pushOutOfBlocks((ClientPlayerEntity) (Object) this, v, v1, v2))
            cir.setReturnValue(false);
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"))
    private void stationapi_sendChatMessage(String string, CallbackInfo ci) {
        PlayerAPI.sendChatMessage((ClientPlayerEntity) (Object) this, string);
    }

    // ???
    /*public float superGetCurrentPlayerStrVsBlock(BlockBase block) {
        return super.getStrengh(block);
    }*/
}
