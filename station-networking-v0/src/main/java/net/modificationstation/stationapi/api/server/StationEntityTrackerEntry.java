package net.modificationstation.stationapi.api.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.util.Util;

@Environment(EnvType.SERVER)
public interface StationEntityTrackerEntry {
    default <T extends Payload> void sendToListeners(PayloadType<PacketByteBuf, T> type, T payload) {
        Util.assertImpl();
    }

    default <T extends Payload> void sendToAround(PayloadType<PacketByteBuf, T> type, T payload) {
        Util.assertImpl();
    }
}
