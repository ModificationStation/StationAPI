package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;

public class StationFlatteningNetworkingImpl implements ModInitializer {

    @Override
    public void onInitialize() {

        // Registering packets for flattened save format
        IdentifiablePacket.register(FlattenedChunkDataS2CPacket.PACKET_ID, true, false, FlattenedChunkDataS2CPacket::new);
        IdentifiablePacket.register(FlattenedMultiBlockChangeS2CPacket.PACKET_ID, true, false, FlattenedMultiBlockChangeS2CPacket::new);
        IdentifiablePacket.register(FlattenedBlockChangeS2CPacket.PACKET_ID, true, false, FlattenedBlockChangeS2CPacket::new);
        IdentifiablePacket.register(FlattenedChunkSectionDataS2CPacket.PACKET_ID, true, false, FlattenedChunkSectionDataS2CPacket::new);
    }
}
