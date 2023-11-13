package net.modificationstation.stationapi.api.server.entity;

import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.util.Identifier;

public interface StationSpawnDataProvider extends CustomSpawnDataProvider {

    Identifier getHandlerIdentifier();

    default void writeToMessage(MessagePacket message) { }

    default void readFromMessage(MessagePacket message) { }
}
