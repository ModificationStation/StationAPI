package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.DisconnectPacket;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
class ClientNetworkHandlerMixin {
    @Inject(
            method = "onHello",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onLoginSuccess(LoginHelloPacket packet, CallbackInfo ci) {
        if (
                StationAPI.EVENT_BUS.post(
                        ServerLoginSuccessEvent.builder()
                                .clientNetworkHandler((ClientNetworkHandler) (Object) this)
                                .loginHelloPacket(packet)
                                .build()
                ).isCanceled()
        ) ci.cancel();
    }

    @Inject(
            method = "onDisconnect",
            at = @At("HEAD")
    )
    private void stationapi_onKicked(DisconnectPacket packet, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                MultiplayerLogoutEvent.builder()
                        .disconnectPacket(packet)
                        .stacktrace(null)
                        .dropped(false)
                        .build()
        );
    }

    @Inject(
            method = "onDisconnected",
            at = @At("HEAD")
    )
    private void stationapi_onDropped(String reason, Object[] stacktrace, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                MultiplayerLogoutEvent.builder()
                        .disconnectPacket(new DisconnectPacket(reason))
                        .stacktrace(stacktrace instanceof String[] strings ? strings : new String[0])
                        .dropped(true)
                        .build()
        );
    }
}
