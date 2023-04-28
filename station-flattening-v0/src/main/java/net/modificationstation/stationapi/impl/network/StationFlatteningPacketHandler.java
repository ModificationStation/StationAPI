package net.modificationstation.stationapi.impl.network;

import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.impl.packet.StationFlatteningBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.StationFlatteningChunkDataS2CPacket;

public abstract class StationFlatteningPacketHandler extends PacketHandler {

    public void onBlockChange(StationFlatteningBlockChangeS2CPacket packet) {
        error(packet);
    }

    public void onMapChunk(StationFlatteningChunkDataS2CPacket packet) {
        error(packet);
    }
}
