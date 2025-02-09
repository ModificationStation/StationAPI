package net.modificationstation.stationapi.impl.client.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.RemapException;
import net.modificationstation.stationapi.impl.registry.sync.RegistrySyncManager;

import java.lang.invoke.MethodHandles;

@Environment(EnvType.CLIENT)
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class ClientRegistryRestorer {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    private static void onDisconnect(MultiplayerLogoutEvent event) throws RemapException {
        RegistrySyncManager.unmap();
    }
}
