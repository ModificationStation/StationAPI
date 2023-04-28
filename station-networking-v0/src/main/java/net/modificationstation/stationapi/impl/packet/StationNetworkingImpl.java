package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.mixin.network.AbstractPacketAccessor;

public class StationNetworkingImpl implements ModInitializer {

    @Override
    public void onInitialize() {

        // Avoid side checks for IdentifiablePacket by numerical ID
        AbstractPacketAccessor.getServerToClientPackets().add(IdentifiablePacket.PACKET_ID);
        AbstractPacketAccessor.getClientToServerPackets().add(IdentifiablePacket.PACKET_ID);
        
        IdentifiablePacket.create(Message.PACKET_ID, true, true, Message::new);
    }
}
