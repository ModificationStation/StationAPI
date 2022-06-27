package net.modificationstation.stationapi.mixin.lifecycle.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.network.PlayerPacketHandlerSetEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerPacketHandler.class)
public class MixinServerPlayerPacketHandler {

    @Inject(
            method = "<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/network/ClientConnection;Lnet/minecraft/entity/player/ServerPlayer;)V",
            at = @At("RETURN")
    )
    private void created(MinecraftServer minecraftServer, ClientConnection arg, ServerPlayer arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(PlayerPacketHandlerSetEvent.builder().player(arg1).build());
    }
}
