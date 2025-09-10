package net.modificationstation.stationapi.api.network.packet;

import net.modificationstation.stationapi.api.network.PacketByteBuf;

public interface Payload<HANDLER> {
    int PACKET_ID = 253;

    PayloadType<? extends PacketByteBuf, ? extends Payload> type();

    void apply(HANDLER handler);
}
