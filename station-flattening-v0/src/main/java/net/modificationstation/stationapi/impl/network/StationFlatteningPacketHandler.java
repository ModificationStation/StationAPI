package net.modificationstation.stationapi.impl.network;

import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedMultiBlockChangeS2CPacket;

public abstract class StationFlatteningPacketHandler extends PacketHandler {

    public void onMapChunk(FlattenedChunkDataS2CPacket packet) {
        error(packet);
    }

    public void onMultiBlockChange(FlattenedMultiBlockChangeS2CPacket arg) {
        this.error(arg);
    }

    public void onBlockChange(FlattenedBlockChangeS2CPacket packet) {
        error(packet);
    }
}
