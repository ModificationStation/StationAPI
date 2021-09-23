package net.modificationstation.stationapi.impl.client.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.LevelSerialRegistry;

import java.io.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientServerRegistryRemapper {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerListeners(MessageListenerRegistryEvent event) {
        event.registry.register(of(MODID, "server_registry_sync"), ClientServerRegistryRemapper::remapRegistries);
    }

    private static void remapRegistries(PlayerBase player, Message message) {
        LOGGER.info("Received level registries from server. Remapping...");
        LevelSerialRegistry.loadAll(NBTIO.readGzipped(new ByteArrayInputStream(message.bytes)));
        LOGGER.info("Successfully synchronized registries with the server.");
    }
}
