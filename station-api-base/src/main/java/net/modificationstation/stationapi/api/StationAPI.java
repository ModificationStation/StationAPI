package net.modificationstation.stationapi.api;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.mine_diver.unsafeevents.EventBus;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.mod.PostInitEvent;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

/**
 * StationAPI main class. Used for some initialization.
 * @author mine_diver
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false, registerStatic = false))
public class StationAPI implements PreLaunchEntrypoint {

    /**
     * StationAPI's instance.
     */
    @Entrypoint.Instance
    public static final StationAPI INSTANCE = Null.get();

    /**
     * StationAPI's ModID.
     */
    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    @Entrypoint.Logger("Station|API")
    public static final Logger LOGGER = Null.get();

    public static final EventBus EVENT_BUS = new EventBus();

    /**
     * Initial setup. Configures logger, entrypoints, and calls the rest of initialization sequence. No Minecraft classes must be referenced here.
     */
    @Override
    public void onPreLaunch() {
        FabricLoader.getInstance().getModContainer("stationapi").ifPresent(modContainer -> EntrypointManager.setup(this, modContainer));
        String name = MODID.getName();
        LOGGER.info("Initializing " + name + "...");
        Configurator.setLevel("mixin", Level.TRACE);
        Configurator.setLevel("Fabric|Loader", Level.INFO);
        LOGGER.info("Loading entrypoints...");
        setupMods();
        LOGGER.info("Finished " + name + " setup.");
    }

    /**
     * Loads main entrypoints, also invokes preInit, init and postInit events. No Minecraft classes must be referenced here.
     */
    private void setupMods() {
        setupEntrypoint(of(MODID, "event_bus"));
        setupEntrypoint(of(MODID, "event_bus_" + FabricLoader.getInstance().getEnvironmentType().name().toLowerCase()));
        LOGGER.info("Invoking PreInit event...");
        EVENT_BUS.post(PreInitEvent.builder().build());
        LOGGER.info("Invoking Init event...");
        EVENT_BUS.post(InitEvent.builder().build());
        LOGGER.info("Invoking PostInit event...");
        EVENT_BUS.post(PostInitEvent.builder().build());
    }

    /**
     * Sets up specified entrypoint.
     * @param entrypoint the entrypoints to iterate over and setup.
     */
    private void setupEntrypoint(Identifier entrypoint) {
        FabricLoader.getInstance().getEntrypointContainers(entrypoint.toString(), Object.class).forEach(entrypointContainer -> {
            LOGGER.info("Setting up \"" + entrypointContainer.getProvider().getMetadata().getId() + "\" \"" + entrypoint + "\" \"" + entrypointContainer.getEntrypoint().getClass().getName() + "\" entrypoint...");
            EntrypointManager.setup(entrypointContainer);
        });
    }
}
