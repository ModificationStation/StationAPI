package net.modificationstation.stationapi.api.server.entity;

import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface StationSpawnDataProvider extends CustomSpawnDataProvider {

    Identifier getHandlerIdentifier();

    default void writeToMessage(Message message) { }

    default void readFromMessage(Message message) { }
}
