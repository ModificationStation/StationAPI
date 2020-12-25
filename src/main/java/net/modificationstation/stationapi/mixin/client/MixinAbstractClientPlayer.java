package net.modificationstation.stationapi.mixin.client;

import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.impl.common.entity.player.PlayerAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
    private void onHandleKeyPress(int i, boolean b, CallbackInfo ci) {
        if (PlayerAPI.handleKeyPress((AbstractClientPlayer) (Object) this, i, b))
            ci.cancel();
    }

    @Redirect(method = "method_141", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;method_687()I"))
    private int redirectGetPlayerArmorValue(PlayerInventory inventoryPlayer) {
        return PlayerAPI.getPlayerArmorValue((AbstractClientPlayer) (Object) this, inventoryPlayer.method_687());
    }

    @Inject(method = "method_1373", at = @At("RETURN"), cancellable = true)
    private void isSneaking(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(PlayerAPI.isSneaking((AbstractClientPlayer) (Object) this, cir.getReturnValue()));
    }

    @Inject(method = "method_1372", at = @At("HEAD"), cancellable = true)
    private void injectPushOutOfBlocks(double v, double v1, double v2, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerAPI.pushOutOfBlocks((AbstractClientPlayer) (Object) this, v, v1, v2))
            cir.setReturnValue(false);
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"))
    private void sendChatMessage(String string, CallbackInfo ci) {
        PlayerAPI.sendChatMessage((AbstractClientPlayer) (Object) this, string);
    }

    // ???
    /*public float superGetCurrentPlayerStrVsBlock(BlockBase block) {
        return super.getStrengh(block);
    }*/
}
