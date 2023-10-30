package net.modificationstation.stationapi.impl.client.network;

import net.modificationstation.stationapi.api.registry.RemapException;
import net.modificationstation.stationapi.api.registry.RemappableRegistry;
import net.modificationstation.stationapi.impl.network.RegistryPacketHandler;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.RemapClientRegistryS2CPacket;
import net.modificationstation.stationapi.impl.registry.sync.RegistrySyncManager;

public class ClientRegistryNetworkHandler extends RegistryPacketHandler {
    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public void onRemapClientRegistry(RemapClientRegistryS2CPacket packet) {
        try {
            RegistrySyncManager.apply(packet.map, RemappableRegistry.RemapMode.REMOTE);
        } catch (RemapException e) {
            throw new RuntimeException(e);
        }
    }
}
