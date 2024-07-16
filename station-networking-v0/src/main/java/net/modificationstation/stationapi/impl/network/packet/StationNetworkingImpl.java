package net.modificationstation.stationapi.impl.network.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.mixin.network.AbstractPacketAccessor;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StationNetworkingImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        // Avoid side checks for IdentifiablePacket by numerical ID
        AbstractPacketAccessor.getClientBoundPackets().add(ManagedPacket.PACKET_ID);
        AbstractPacketAccessor.getServerBoundPackets().add(ManagedPacket.PACKET_ID);

        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("message"), MessagePacket.TYPE);
    }
}
