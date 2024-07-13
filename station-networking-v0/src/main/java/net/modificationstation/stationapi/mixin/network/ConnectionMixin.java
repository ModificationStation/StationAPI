package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Connection.class)
class ConnectionMixin {
    @Redirect(
            method = "method_1129",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/Packet;apply(Lnet/minecraft/network/NetworkHandler;)V"
            )
    )
    private void stationapi_ifIdentifiable(Packet instance, NetworkHandler packetHandler) {
        instance.apply(
                instance instanceof ManagedPacket<?> managedPacket ?
                        managedPacket.getType().getHandler().orElse(packetHandler) :
                        packetHandler
        );
    }
}
