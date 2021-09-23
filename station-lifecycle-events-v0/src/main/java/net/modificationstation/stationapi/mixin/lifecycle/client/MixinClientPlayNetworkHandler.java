package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {

    @Shadow public abstract void method_1647();

    @Inject(
            method = "onLoginRequest(Lnet/minecraft/packet/login/LoginRequest0x1Packet;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onLoginSuccess(LoginRequest0x1Packet packet, CallbackInfo ci) {
        if (StationAPI.EVENT_BUS.post(new ServerLoginSuccessEvent((ClientPlayNetworkHandler) (Object) this, packet)).isCancelled())
            ci.cancel();
    }
}
