package net.modificationstation.stationapi.impl.network;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.init.InitFinishedEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class VanillaChecker {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    /**
     * A set of mods that are required client side when joining a server.
     */
    public static final Set<ModContainer> CLIENT_REQUIRED_MODS = new HashSet<>();

    /**
     * A set of mods that are required on the server side when joining a server.
     */
    public static final Set<ModContainer> SERVER_REQUIRED_MODS = new HashSet<>();

    @EventListener
    private static void init(InitFinishedEvent event) {
        LOGGER.info("Checking mod metadata for client/server-side requirements...");

        String oldVerifyClientKey = NAMESPACE + ":verify_client";
        String requiredOnClientKey = NAMESPACE + ":required_on_client";
        String requiredOnServerKey = NAMESPACE + ":required_on_server";
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (modMetadata.containsCustomValue(requiredOnClientKey) && modMetadata.getCustomValue(requiredOnClientKey).getAsBoolean())
                CLIENT_REQUIRED_MODS.add(modContainer);
            else if (modMetadata.containsCustomValue(oldVerifyClientKey) && modMetadata.getCustomValue(oldVerifyClientKey).getAsBoolean())
                CLIENT_REQUIRED_MODS.add(modContainer);
            if (modMetadata.containsCustomValue(requiredOnServerKey) && modMetadata.getCustomValue(requiredOnServerKey).getAsBoolean())
                SERVER_REQUIRED_MODS.add(modContainer);
        });
        LOGGER.info("Found {} mods required on client, {} mods required on server.", CLIENT_REQUIRED_MODS.size(), SERVER_REQUIRED_MODS.size());
    }
}