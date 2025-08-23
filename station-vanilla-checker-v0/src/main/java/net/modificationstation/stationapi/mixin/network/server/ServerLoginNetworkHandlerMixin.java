package net.modificationstation.stationapi.mixin.network.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorld;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerLoginNetworkHandler.class)
class ServerLoginNetworkHandlerMixin {
    @Inject(
            method = "accept",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/network/Connection;Lnet/minecraft/entity/player/ServerPlayerEntity;)V",
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_checkModded(LoginHelloPacket arg, CallbackInfo ci, ServerPlayerEntity var2, ServerWorld var3, Vec3i var4, ServerPlayNetworkHandler var5) {
        ModdedPacketHandler moddedPacketHandler = ((ModdedPacketHandler) this);
        if (moddedPacketHandler.isModded())
            ((ModdedPacketHandlerSetter) var5).setModded(moddedPacketHandler.getMods());
    }
}
