package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.packet.StationFlatteningBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.StationFlatteningChunkDataS2CPacket;

public class StationFlatteningClientNetworkingImpl implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PacketHandler handler = new StationClientPlayNetworkHandler();
        IdentifiablePacket.setHandler(StationFlatteningBlockChangeS2CPacket.PACKET_ID, handler);
        IdentifiablePacket.setHandler(StationFlatteningChunkDataS2CPacket.PACKET_ID, handler);
    }
}
