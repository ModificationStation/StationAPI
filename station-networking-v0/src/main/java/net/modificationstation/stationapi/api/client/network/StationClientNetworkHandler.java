package net.modificationstation.stationapi.api.client.network;

import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.util.Util;

public interface StationClientNetworkHandler {

    default <P extends Payload<?>> void sendPayload(P payload) {
        sendPayload((PayloadType<PacketByteBuf, P>) payload.type(), payload);
    }

    default <P extends Payload<?>> void sendPayload(PayloadType<PacketByteBuf, P> type, P payload) {
        Util.assertImpl();
    }
}
