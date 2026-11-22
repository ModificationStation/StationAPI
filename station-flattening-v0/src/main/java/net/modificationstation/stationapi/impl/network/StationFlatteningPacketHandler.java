package net.modificationstation.stationapi.impl.network;

import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockUpdateS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDeltaUpdateS2CPacket;

public abstract class StationFlatteningPacketHandler extends NetworkHandler {
    public void handleChunkData(FlattenedChunkDataS2CPacket packet) {
        handle(packet);
    }

    public void onChunkDeltaUpdate(FlattenedChunkDeltaUpdateS2CPacket packet) {
        handle(packet);
    }

    public void onBlockUpdate(FlattenedBlockUpdateS2CPacket packet) {
        handle(packet);
    }

    public void handleChunkSectionData(FlattenedChunkSectionDataS2CPacket packet) {
        handle(packet);
    }
}
