package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.NetworkHandler;
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
        FlattenedChunkDataS2CPacket.TYPE.setHandler(handler);
        FlattenedMultiBlockChangeS2CPacket.TYPE.setHandler(handler);
        FlattenedBlockChangeS2CPacket.TYPE.setHandler(handler);
        FlattenedChunkSectionDataS2CPacket.TYPE.setHandler(handler);
    }
}
