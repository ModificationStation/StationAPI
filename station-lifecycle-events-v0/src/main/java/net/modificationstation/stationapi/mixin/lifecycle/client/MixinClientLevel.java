package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.misc.Disconnect0xFFPacket;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientLevel.class)
public class MixinClientLevel {

    @Redirect(
            method = "disconnect()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientPlayNetworkHandler;method_1646(Lnet/minecraft/packet/AbstractPacket;)V")
    )
    private void onDisconnect(ClientPlayNetworkHandler clientPlayNetworkHandler, AbstractPacket arg) {
        StationAPI.EVENT_BUS.post(
                MultiplayerLogoutEvent.builder()
                        .packet((Disconnect0xFFPacket) arg)
                        .stacktrace(null)
                        .dropped(false)
                        .build()
        );
        clientPlayNetworkHandler.method_1646(arg);
    }
}
