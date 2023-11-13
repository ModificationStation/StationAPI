package net.modificationstation.stationapi.mixin.lifecycle.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.network.PlayerPacketHandlerSetEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
class ServerPlayNetworkHandlerMixin {
    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void stationapi_created(MinecraftServer minecraftServer, Connection arg, ServerPlayerEntity arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(PlayerPacketHandlerSetEvent.builder().player(arg1).build());
    }
}
