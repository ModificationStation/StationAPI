package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.class_454;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.DisconnectPacket;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_454.class)
class ClientWorldMixin {
    @Redirect(
            method = "method_293",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientNetworkHandler;method_1646(Lnet/minecraft/network/packet/Packet;)V"
            )
    )
    private void stationapi_onDisconnect(ClientNetworkHandler clientPlayNetworkHandler, Packet arg) {
        StationAPI.EVENT_BUS.post(
                MultiplayerLogoutEvent.builder()
                        .disconnectPacket((DisconnectPacket) arg)
                        .stacktrace(null)
                        .dropped(false)
                        .build()
        );
        clientPlayNetworkHandler.method_1646(arg);
    }
}
