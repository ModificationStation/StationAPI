package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.minecraft.packet.misc.Disconnect0xFFPacket;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.benjiweber.expressions.tuple.BiTuple;

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

    @Inject(
            method = "onDisconnect(Lnet/minecraft/packet/misc/Disconnect0xFFPacket;)V",
            at = @At("HEAD")
    )
    private void onKicked(Disconnect0xFFPacket packet, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new MultiplayerLogoutEvent(packet, null, false));
    }

    @Inject(
            method = "onClose(Ljava/lang/String;[Ljava/lang/Object;)V",
            at = @At("HEAD")
    )
    private void onDropped(String reason, Object[] stacktrace, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new MultiplayerLogoutEvent(new Disconnect0xFFPacket(reason), (String[]) stacktrace, true));
    }
}
