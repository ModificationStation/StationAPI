package net.modificationstation.stationapi.api;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.mine_diver.unsafeevents.EventBus;
import net.modificationstation.stationapi.api.config.Category;
import net.modificationstation.stationapi.api.config.Configuration;
import net.modificationstation.stationapi.api.config.Property;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.mod.PostInitEvent;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.impl.config.CategoryImpl;
import net.modificationstation.stationapi.impl.config.PropertyImpl;
import net.modificationstation.stationapi.impl.factory.EnumFactory;
import net.modificationstation.stationapi.impl.factory.GeneralFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.*;

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

    @Entrypoint.Config
    public static final Configuration CONFIG = Null.get();

    /**
     * A set of mods that need client-side verification when the client joins server.
     */
    private final Set<ModContainer> modsToVerifyOnClient = new HashSet<>();

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
        LOGGER.info("Setting up API...");
        setupAPI();
        LOGGER.info("Loading mods...");
        setupMods();
        LOGGER.info("Finished " + name + " setup");
    }

    /**
     * Performs some API setup. Most likely will be removed due to API becoming less abstract. No Minecraft classes must be referenced here.
     */
    public void setupAPI() {
        LOGGER.info("Setting up GeneralFactory...");
        net.modificationstation.stationapi.api.factory.GeneralFactory generalFactory = net.modificationstation.stationapi.api.factory.GeneralFactory.INSTANCE;
        generalFactory.setHandler(new GeneralFactory());
        generalFactory.addFactory(Category.class, args -> new CategoryImpl((String) args[0]));
        generalFactory.addFactory(Property.class, args -> new PropertyImpl((String) args[0]));
        LOGGER.info("Setting up EnumFactory...");
        net.modificationstation.stationapi.api.factory.EnumFactory.INSTANCE.setHandler(new EnumFactory());
    }

    /**
     * Loads main entrypoints, also invokes preInit, init and postInit events. No Minecraft classes must be referenced here.
     */
    private void setupMods() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        fabricLoader.getEntrypointContainers(Identifier.of(MODID, "event_bus").toString(), Object.class).forEach(EntrypointManager::setup);
        fabricLoader.getEntrypointContainers(Identifier.of(MODID, "event_bus_" + fabricLoader.getEnvironmentType().name().toLowerCase()).toString(), Object.class).forEach(EntrypointManager::setup);
        LOGGER.info("Gathering mods that require client verification...");
        String value = MODID + ":verify_client";
        fabricLoader.getAllMods().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (modMetadata.containsCustomValue(value) && modMetadata.getCustomValue(value).getAsBoolean())
                modsToVerifyOnClient.add(modContainer);
        });
        LOGGER.info("Invoking PreInit event...");
        EVENT_BUS.post(new PreInitEvent());
        LOGGER.info("Invoking Init event...");
        EVENT_BUS.post(new InitEvent());
        LOGGER.info("Invoking PostInit event...");
        EVENT_BUS.post(new PostInitEvent());
    }

    /**
     * Returns the set of mods that need client-side verification when the client joins server.
     * @return the set of mods that need client-side verification when the client joins server.
     */
    public Set<ModContainer> getModsToVerifyOnClient() {
        return Collections.unmodifiableSet(modsToVerifyOnClient);
    }
}
