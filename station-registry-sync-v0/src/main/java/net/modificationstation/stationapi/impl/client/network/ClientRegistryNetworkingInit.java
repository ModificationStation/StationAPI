package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.RemapClientRegistryS2CPacket;

public final class ClientRegistryNetworkingInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetworkHandler handler = new ClientRegistryNetworkHandler();
        IdentifiablePacket.setHandler(RemapClientRegistryS2CPacket.PACKET_ID, handler);
    }
}
