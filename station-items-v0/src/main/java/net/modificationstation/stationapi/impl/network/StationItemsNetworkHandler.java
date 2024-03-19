package net.modificationstation.stationapi.impl.network;

import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;

public abstract class StationItemsNetworkHandler extends NetworkHandler {
    public void onItemEntitySpawn(StationItemEntitySpawnS2CPacket packet) {
        handle(packet);
    }
}
