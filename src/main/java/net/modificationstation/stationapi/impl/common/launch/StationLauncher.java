package net.modificationstation.stationapi.impl.common.launch;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.registry.ModID;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class StationLauncher implements PreLaunchEntrypoint {

    private static final Logger LOGGER = LogManager.getFormatterLogger("Station|Launcher");

    @Override
    public void onPreLaunch() {
        Configurator.setLevel("Station|Launcher", Level.INFO);
        FabricLoader fabricLoader = FabricLoader.getInstance();
        LOGGER.info("Trying to get ModContainer...");
        fabricLoader.getModContainer("stationapi").ifPresent(modContainer -> {
            ModMetadata stationData = modContainer.getMetadata();
            String name = stationData.getName();
            LOGGER.info("Got the " + name + " metadata");
            StationAPI station = StationAPI.INSTANCE;
            LOGGER.info("Trying to load " + name + " entrypoints...");
            fabricLoader.getEntrypoints(stationData.getId() + ":" + fabricLoader.getEnvironmentType().name().toLowerCase(), StationAPI.class).stream().findFirst().ifPresent(station1 -> {
                LOGGER.info("Found the first entrypoint! Initializing...");
                station.setHandler(station1);
                station.setModID(ModID.of(modContainer));
                LOGGER.info("Running setup...");
                station.setup();
                LOGGER.info("Done!");
            });
        });
    }
}
