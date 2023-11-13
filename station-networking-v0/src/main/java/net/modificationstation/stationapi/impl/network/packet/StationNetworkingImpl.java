package net.modificationstation.stationapi.impl.network.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.mixin.network.AbstractPacketAccessor;

public class StationNetworkingImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        // Avoid side checks for IdentifiablePacket by numerical ID
        AbstractPacketAccessor.getClientBoundPackets().add(IdentifiablePacket.PACKET_ID);
        AbstractPacketAccessor.getServerBoundPackets().add(IdentifiablePacket.PACKET_ID);
        
        IdentifiablePacket.register(MessagePacket.PACKET_ID, true, true, MessagePacket::new);
    }
}
