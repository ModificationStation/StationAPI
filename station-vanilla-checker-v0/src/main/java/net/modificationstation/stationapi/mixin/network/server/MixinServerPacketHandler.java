package net.modificationstation.stationapi.mixin.network.server;

import net.minecraft.class_73;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerLoginNetworkHandler.class)
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
    private void checkModded(LoginHelloPacket arg, CallbackInfo ci, ServerPlayerEntity var2, class_73 var3, Vec3i var4, ServerPlayNetworkHandler var5) {
        if (((ModdedPacketHandler) this).isModded())
            ((ModdedPacketHandlerSetter) var5).setModded();
    }
}
