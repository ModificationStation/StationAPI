package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.packet.handshake.HandshakePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HandshakePacket.class)
public class HandshakePacketMixin {
    @ModifyArg(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/handshake/HandshakePacket;readString(Ljava/io/DataInputStream;I)Ljava/lang/String;"), index = 1)
    private int modifyMaxStringForGlassNetworking(int par2) {
        return Short.MAX_VALUE;
    }
}
