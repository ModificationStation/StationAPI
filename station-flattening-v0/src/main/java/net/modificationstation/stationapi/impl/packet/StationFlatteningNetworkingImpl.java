package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;

public class StationFlatteningNetworkingImpl implements ModInitializer {

    @Override
    public void onInitialize() {

        // Registering packets for flattened save format
        IdentifiablePacket.create(FlattenedChunkDataS2CPacket.PACKET_ID, true, false, FlattenedChunkDataS2CPacket::new);
        IdentifiablePacket.create(FlattenedMultiBlockChangeS2CPacket.PACKET_ID, true, false, FlattenedMultiBlockChangeS2CPacket::new);
        IdentifiablePacket.create(FlattenedBlockChangeS2CPacket.PACKET_ID, true, false, FlattenedBlockChangeS2CPacket::new);
    }
}
