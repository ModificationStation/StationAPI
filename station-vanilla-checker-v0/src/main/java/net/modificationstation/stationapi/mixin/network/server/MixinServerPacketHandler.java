package net.modificationstation.stationapi.mixin.network.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerPacketHandler;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPacketHandler.class)
public class MixinServerPacketHandler {

    @Inject(
            method = "complete(Lnet/minecraft/packet/login/LoginRequest0x1Packet;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerPacketHandler;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/network/ClientConnection;Lnet/minecraft/entity/player/ServerPlayer;)V",
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void checkModded(LoginRequest0x1Packet arg, CallbackInfo ci, ServerPlayer var2, ServerLevel var3, Vec3i var4, ServerPlayerPacketHandler var5) {
        if (((ModdedPacketHandler) this).isModded())
            ((ModdedPacketHandlerSetter) var5).setModded();
    }
}
