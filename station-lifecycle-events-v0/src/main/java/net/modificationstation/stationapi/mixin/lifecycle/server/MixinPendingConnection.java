package net.modificationstation.stationapi.mixin.lifecycle.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.minecraft.server.network.ServerPacketHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.network.PlayerAttemptLoginEvent;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPacketHandler.class)
public class MixinPendingConnection {

    @Inject(method = "complete(Lnet/minecraft/packet/login/LoginRequest0x1Packet;)V", at = @At("HEAD"))
    private void handleLogin(LoginRequest0x1Packet arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new PlayerAttemptLoginEvent((ServerPacketHandler) (Object) this, arg));
    }

    @Inject(method = "complete(Lnet/minecraft/packet/login/LoginRequest0x1Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayer;method_317()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void afterLogin(LoginRequest0x1Packet arg, CallbackInfo ci, ServerPlayer var2) {
        StationAPI.EVENT_BUS.post(new PlayerLoginEvent(arg, var2));
    }
}
