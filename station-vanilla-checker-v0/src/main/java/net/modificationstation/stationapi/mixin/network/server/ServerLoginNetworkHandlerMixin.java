package net.modificationstation.stationapi.mixin.network.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLoginNetworkHandler.class)
class ServerLoginNetworkHandlerMixin {
    @WrapOperation(
            method = "accept",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/network/Connection;Lnet/minecraft/entity/player/ServerPlayerEntity;)Lnet/minecraft/server/network/ServerPlayNetworkHandler;"
            )
    )
    private ServerPlayNetworkHandler stationapi_checkModded(
            MinecraftServer server, Connection connection, ServerPlayerEntity player, Operation<ServerPlayNetworkHandler> original
    ) {
        final var serverPlayNetworkHandler = original.call(server, connection, player);
        if (this instanceof ModdedPacketHandler serverLoginNetworkHandler && serverLoginNetworkHandler.isModded())
            ((ModdedPacketHandlerSetter) serverPlayNetworkHandler).setModded(serverLoginNetworkHandler.getMods());
        return serverPlayNetworkHandler;
    }
}
