package net.modificationstation.stationapi.mixin.network.server;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.StationConnection;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.server.network.StationServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements StationServerPlayNetworkHandler {
    @Shadow public Connection field_917;

    @Shadow private int field_922;

    @Shadow private int field_921;

    @Override
    public <T extends Payload> void sendPayload(PayloadType<PacketByteBuf, T> type, T payload) {
        ((StationConnection) this.field_917).sendPayload(type, payload);
        this.field_922 = this.field_921;
    }
}
