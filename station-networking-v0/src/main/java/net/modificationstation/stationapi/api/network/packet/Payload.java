package net.modificationstation.stationapi.api.network.packet;

import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.PayloadHandler;

public interface Payload<H extends PayloadHandler> {
    int PACKET_ID = 252;

    PayloadType<? extends PacketByteBuf, ? extends Payload> type();

    void handle(H handler);
}
