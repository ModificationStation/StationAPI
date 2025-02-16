package net.modificationstation.stationapi.api;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.mine_diver.unsafeevents.EventBus;
import net.mine_diver.unsafeevents.eventbus.ManagedEventBus;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.ApiStatus;

/**
 * StationAPI main class. Used for some initialization.
 * @author mine_diver
 */
@Entrypoint(eventBus = @EventBusPolicy(
        registerInstance = false,
        registerStatic = false
))
public class StationAPI implements PreLaunchEntrypoint {
    /**
     * StationAPI's namespace.
     */
    public static final Namespace NAMESPACE = Namespace.of("stationapi");

    public static final Logger LOGGER = NAMESPACE.getLogger("Station|API");

    @ApiStatus.Internal
    public static final String INTERNAL_PHASE = "stationapi:internal";

    @ApiStatus.Internal
    private static final ManagedEventBus MANAGED_EVENT_BUS = new ManagedEventBus();

    public static final EventBus EVENT_BUS = MANAGED_EVENT_BUS;

    /**
     * Initial setup. Configures logger, entrypoints, and calls the rest of initialization sequence. No Minecraft classes must be referenced here.
     */
    @Override
    public void onPreLaunch() {
        String name = NAMESPACE.getName();
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
        setupEntrypoint(NAMESPACE.id("event_bus"));
        setupEntrypoint(NAMESPACE.id("event_bus_" + FabricLoader.getInstance().getEnvironmentType().name().toLowerCase()));
        LOGGER.info("Invoking Init event...");
        EVENT_BUS.post(InitEvent.builder().build());
    }

    /**
     * Sets up specified entrypoint.
     * @param entrypoint the entrypoints to iterate over and setup.
     */
    private void setupEntrypoint(Identifier entrypoint) {
        MANAGED_EVENT_BUS.disableDispatch(
                "During the registration of listeners, the event dispatch to StationAPI's event bus is temporarily disabled to prevent listeners from causing an illegal event dispatch. " +
                        "This helps ensure that the event bus behaves predictably and without dispatching an event while not all listeners have been registered."
        );
        FabricLoader.getInstance().getEntrypointContainers(entrypoint.toString(), Object.class).forEach(entrypointContainer -> {
            LOGGER.info("Setting up \"" + entrypointContainer.getProvider().getMetadata().getId() + "\" \"" + entrypoint + "\" \"" + entrypointContainer.getEntrypoint().getClass().getName() + "\" entrypoint...");
            EntrypointManager.setup(entrypointContainer);
        });
        MANAGED_EVENT_BUS.enableDispatch();
    }
}
