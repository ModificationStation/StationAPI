package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedMultiBlockChangeS2CPacket;

public class FlattenedClientNetworkingInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Overriding vanilla packet handler with the StationAPI one
        // for flattened packets
        NetworkHandler handler = new FlattenedClientPlayNetworkHandler();
        IdentifiablePacket.setHandler(FlattenedChunkDataS2CPacket.PACKET_ID, handler);
        IdentifiablePacket.setHandler(FlattenedMultiBlockChangeS2CPacket.PACKET_ID, handler);
        IdentifiablePacket.setHandler(FlattenedBlockChangeS2CPacket.PACKET_ID, handler);
        IdentifiablePacket.setHandler(FlattenedChunkSectionDataS2CPacket.PACKET_ID, handler);
    }
}
