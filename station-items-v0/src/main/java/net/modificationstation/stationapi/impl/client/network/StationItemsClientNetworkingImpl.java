package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationEntityEquipmentUpdateS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;

public class StationItemsClientNetworkingImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetworkHandler handler = new StationItemsClientNetworkHandler();
        IdentifiablePacket.setHandler(StationItemEntitySpawnS2CPacket.PACKET_ID, handler);
        IdentifiablePacket.setHandler(StationEntityEquipmentUpdateS2CPacket.PACKET_ID, handler);
    }
}
