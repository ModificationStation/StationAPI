package net.modificationstation.stationloader.impl.common.launch;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationloader.api.common.StationLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class StationLoaderLauncher implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        Configurator.setLevel("StationLoader|Launcher", Level.INFO);
        FabricLoader fabricLoader = FabricLoader.getInstance();
        LOGGER.info("Trying to get ModContainer...");
        fabricLoader.getModContainer("stationloader").ifPresent(modContainer -> {
            ModMetadata slData = modContainer.getMetadata();
            String name = slData.getName();
            LOGGER.info("Got the " + name + " metadata");
            StationLoader stationLoader = StationLoader.INSTANCE;
            LOGGER.info("Trying to load " + name + " entrypoints...");
            fabricLoader.getEntrypoints(slData.getId() + ":" + fabricLoader.getEnvironmentType().name().toLowerCase(), StationLoader.class).stream().findFirst().ifPresent(stationLoader1 -> {
                LOGGER.info("Found the first entrypoint! Initializing...");
                stationLoader.setHandler(stationLoader1);
                stationLoader.setContainer(modContainer);
                LOGGER.info("Running setup...");
                stationLoader.setup();
                LOGGER.info("Done!");
            });
        });
    }

    private static final Logger LOGGER = LogManager.getFormatterLogger("StationLoader|Launcher");
}
