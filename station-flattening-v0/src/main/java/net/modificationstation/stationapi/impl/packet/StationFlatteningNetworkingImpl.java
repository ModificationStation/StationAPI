package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;

public class StationFlatteningNetworkingImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        IdentifiablePacket.create(StationFlatteningChunkDataS2CPacket.PACKET_ID, true, false, StationFlatteningChunkDataS2CPacket::new);
        IdentifiablePacket.create(StationFlatteningBlockChangeS2CPacket.PACKET_ID, true, false, StationFlatteningBlockChangeS2CPacket::new);
    }
}
