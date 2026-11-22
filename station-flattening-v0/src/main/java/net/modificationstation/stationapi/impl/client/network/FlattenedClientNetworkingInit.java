package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockUpdateS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDeltaUpdateS2CPacket;

public class FlattenedClientNetworkingInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Overriding vanilla packet handler with the StationAPI one
        // for flattened packets
        NetworkHandler handler = new FlattenedClientPlayNetworkHandler();
        FlattenedChunkDataS2CPacket.TYPE.setHandler(handler);
        FlattenedChunkDeltaUpdateS2CPacket.TYPE.setHandler(handler);
        FlattenedBlockUpdateS2CPacket.TYPE.setHandler(handler);
        FlattenedChunkSectionDataS2CPacket.TYPE.setHandler(handler);
    }
}
