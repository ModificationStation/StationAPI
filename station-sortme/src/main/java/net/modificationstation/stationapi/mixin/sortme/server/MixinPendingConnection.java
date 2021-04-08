package net.modificationstation.stationapi.mixin.sortme.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.handshake.HandshakeC2S;
import net.minecraft.server.network.PendingConnection;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.network.HandshakeSuccessEvent;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PendingConnection.class)
public class MixinPendingConnection {

    @Inject(method = "complete(Lnet/minecraft/packet/handshake/HandshakeC2S;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayer;method_317()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void afterLogin(HandshakeC2S arg, CallbackInfo ci, ServerPlayer var2) {
        StationAPI.EVENT_BUS.post(new PlayerLoginEvent(var2));
    }

    @Inject(method = "complete(Lnet/minecraft/packet/handshake/HandshakeC2S;)V", at = @At("HEAD"))
    private void handleLogin(HandshakeC2S arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new HandshakeSuccessEvent((PendingConnection) (Object) this, arg));
    }
}
