package net.modificationstation.stationapi.impl.client.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtIo;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.registry.legacy.WorldLegacyRegistry;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodHandles;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ClientServerRegistryRemapper {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerListeners(MessageListenerRegistryEvent event) {
        event.register(NAMESPACE.id("server_registry_sync"), ClientServerRegistryRemapper::remapRegistries);
    }

    private static void remapRegistries(PlayerEntity player, MessagePacket message) {
        LOGGER.info("Received level registries from server. Remapping...");
        WorldLegacyRegistry.loadAll(NbtIo.readCompressed(new ByteArrayInputStream(message.bytes)));
        LOGGER.info("Successfully synchronized registries with the server.");
    }
}
