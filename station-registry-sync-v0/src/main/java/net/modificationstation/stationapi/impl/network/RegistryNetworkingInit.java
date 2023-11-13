package net.modificationstation.stationapi.impl.network;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.RemapClientRegistryS2CPacket;

public final class RegistryNetworkingInit implements ModInitializer {
    @Override
    public void onInitialize() {
        IdentifiablePacket.register(RemapClientRegistryS2CPacket.PACKET_ID, true, false, RemapClientRegistryS2CPacket::new);
    }
}
