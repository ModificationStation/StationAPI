package net.modificationstation.stationapi.mixin.lifecycle.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.network.PlayerAttemptLoginEvent;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerLoginNetworkHandler.class)
class ServerLoginNetworkHandlerMixin {
    @Inject(
            method = "accept",
            at = @At("HEAD")
    )
    private void stationapi_handleLogin(LoginHelloPacket arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                PlayerAttemptLoginEvent.builder()
                        .serverLoginNetworkHandler((ServerLoginNetworkHandler) (Object) this)
                        .loginHelloPacket(arg)
                        .build()
        );
    }

    @Inject(
            method = "accept",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ServerPlayerEntity;initScreenHandler()V",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_afterLogin(LoginHelloPacket arg, CallbackInfo ci, ServerPlayerEntity var2) {
        StationAPI.EVENT_BUS.post(
                PlayerLoginEvent.builder()
                        .loginHelloPacket(arg)
                        .player(var2).build()
        );
    }
}
