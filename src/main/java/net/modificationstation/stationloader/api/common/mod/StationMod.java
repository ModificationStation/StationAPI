package net.modificationstation.stationloader.api.common.mod;

import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationloader.api.common.config.Configuration;
import net.modificationstation.stationloader.api.common.event.mod.PreInit;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public interface StationMod extends PreInit {

    default Logger getLogger() {
        return loggers.get(this);
    }

    default void setLogger(Logger log) {
        loggers.put(this, log);
    }

    Map<StationMod, Logger> loggers = new HashMap<>();

    default Path getConfigPath() {
        return configPaths.get(this);
    }

    default void setConfigPath(Path configPath) {
        configPaths.put(this, configPath);
    }

    Map<StationMod, Path> configPaths = new HashMap<>();

    default Configuration getDefaultConfig() {
        return defaultConfigs.get(this);
    }

    default void setDefaultConfig(Configuration config) {
        defaultConfigs.put(this, config);
    }

    Map<StationMod, Configuration> defaultConfigs = new HashMap<>();

    default ModMetadata getData() {
        return metadatas.get(this);
    }

    default void setData(ModMetadata data) {
        metadatas.put(this, data);
    }

    Map<StationMod, ModMetadata> metadatas = new HashMap<>();
}
