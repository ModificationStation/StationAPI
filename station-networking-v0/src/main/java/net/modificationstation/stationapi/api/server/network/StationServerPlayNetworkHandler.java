package net.modificationstation.stationapi.api.server.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.util.Util;

@Environment(EnvType.SERVER)
public interface StationServerPlayNetworkHandler {
    default <T extends Payload> void sendPayload(PayloadType<PacketByteBuf, T> type, T payload) {
        Util.assertImpl();
    }
}
