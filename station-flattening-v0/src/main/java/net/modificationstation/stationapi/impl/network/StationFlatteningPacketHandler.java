package net.modificationstation.stationapi.impl.network;

import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedMultiBlockChangeS2CPacket;

public abstract class StationFlatteningPacketHandler extends NetworkHandler {

    public void onMapChunk(FlattenedChunkDataS2CPacket packet) {
        handle(packet);
    }

    public void onMultiBlockChange(FlattenedMultiBlockChangeS2CPacket arg) {
        this.handle(arg);
    }

    public void onBlockChange(FlattenedBlockChangeS2CPacket packet) {
        handle(packet);
    }

    public void onChunkSection(FlattenedChunkSectionDataS2CPacket packet) {
        handle(packet);
    }
}
