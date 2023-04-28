package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.server.network.ClientConnection;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.packet.IdentifiablePacketImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Redirect(
            method = "method_1129",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/packet/AbstractPacket;apply(Lnet/minecraft/network/PacketHandler;)V"
            )
    )
    private void ifIdentifiable(AbstractPacket instance, PacketHandler packetHandler) {
        instance.apply(
                instance instanceof IdentifiablePacket identifiablePacket ?
                        IdentifiablePacketImpl.HANDLERS.getOrDefault(identifiablePacket.getId(), packetHandler) :
                        packetHandler
        );
    }
}
