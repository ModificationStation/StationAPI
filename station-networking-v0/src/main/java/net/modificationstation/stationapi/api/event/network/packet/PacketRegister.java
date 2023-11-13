package net.modificationstation.stationapi.api.event.network.packet;

import net.minecraft.network.packet.Packet;

public interface PacketRegister {
    void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends Packet> packetClass);
}
