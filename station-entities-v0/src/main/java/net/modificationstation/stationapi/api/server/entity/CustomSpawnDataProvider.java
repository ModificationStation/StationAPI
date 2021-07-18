package net.modificationstation.stationapi.api.server.entity;

import net.minecraft.packet.AbstractPacket;

public interface CustomSpawnDataProvider {

    AbstractPacket getSpawnData();
}
