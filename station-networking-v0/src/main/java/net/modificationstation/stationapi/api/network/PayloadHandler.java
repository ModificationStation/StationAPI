package net.modificationstation.stationapi.api.network;

import net.modificationstation.stationapi.api.network.packet.Payload;

public interface PayloadHandler {
    default void handle(Payload<?> payload) {}
}
