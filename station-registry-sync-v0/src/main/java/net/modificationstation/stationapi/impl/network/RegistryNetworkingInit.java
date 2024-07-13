package net.modificationstation.stationapi.impl.network;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.RemapClientRegistryS2CPacket;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class RegistryNetworkingInit implements ModInitializer {
    @Override
    public void onInitialize() {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("registry/remap_client"), RemapClientRegistryS2CPacket.TYPE);
    }
}
