package net.modificationstation.stationapi.impl.network;

import com.google.common.hash.Hashing;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.resource.language.LanguageManager;
import net.modificationstation.stationapi.api.util.Null;

import java.util.HashSet;
import java.util.Set;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class VanillaChecker {

    @Entrypoint.Namespace
    private static final Namespace MODID = Null.get();

    public static final long MASK = Hashing.sipHash24().hashUnencodedChars(StationAPI.NAMESPACE.toString()).asLong();

    /**
     * A set of mods that need client-side verification when the client joins server.
     */
    public static final Set<ModContainer> CLIENT_REQUIRED_MODS = new HashSet<>();

    @EventListener
    private static void init(PreInitEvent event) {
        LOGGER.info("Adding vanilla checker lang folder...");
        LanguageManager.addPath("/assets/" + MODID + "/lang", StationAPI.NAMESPACE);
        LOGGER.info("Gathering mods that require client verification...");
        String value = StationAPI.NAMESPACE + ":verify_client";
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (modMetadata.containsCustomValue(value) && modMetadata.getCustomValue(value).getAsBoolean())
                CLIENT_REQUIRED_MODS.add(modContainer);
        });
    }
}