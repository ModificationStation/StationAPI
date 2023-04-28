package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.packet.Message;

public class StationNetworkingImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        IdentifiablePacket.create(Message.PACKET_ID, true, true, Message::new);
    }
}
