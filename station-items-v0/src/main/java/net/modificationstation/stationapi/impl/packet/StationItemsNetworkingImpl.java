package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;

public class StationItemsNetworkingImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        IdentifiablePacket.register(StationInventoryS2CPacket.PACKET_ID, true, false, StationInventoryS2CPacket::new);
        IdentifiablePacket.register(StationScreenHandlerSlotUpdateS2CPacket.PACKET_ID, true, false, StationScreenHandlerSlotUpdateS2CPacket::new);
    }
}
