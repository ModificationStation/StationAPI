package net.modificationstation.stationapi.impl.client.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.LevelSerialRegistry;

import java.io.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientServerRegistryRemapper {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void remapClientRegistry(MessageListenerRegistryEvent event) {
        event.registry.register(of(MODID, "server_registry_sync"), (player, message) -> LevelSerialRegistry.loadAll(NBTIO.readGzipped(new ByteArrayInputStream(message.bytes))));
    }
}
