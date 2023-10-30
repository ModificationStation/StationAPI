package net.modificationstation.stationapi.impl.server.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.api.server.event.network.PlayerPacketHandlerSetEvent;
import net.modificationstation.stationapi.impl.registry.sync.RegistrySyncManager;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ServerRegistrySynchronizer {
    @EventListener
    private static void sendLevelRegistry(PlayerPacketHandlerSetEvent event) {
        if (((ModdedPacketHandler) event.player.packetHandler).isModded())
            RegistrySyncManager.configureClient(event.player);
    }
}
