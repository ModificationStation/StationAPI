package net.modificationstation.stationloader.api.common.util;

import net.fabricmc.loader.api.ModContainer;
import net.modificationstation.stationloader.api.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public interface ModCore {

    default Logger getLogger() {
        return loggers.get(this);
    }

    default void setLogger(Logger log) {
        loggers.put(this, log);
    }

    Map<ModCore, Logger> loggers = new HashMap<>();

    default Path getConfigPath() {
        return configPaths.get(this);
    }

    default void setConfigPath(Path configPath) {
        configPaths.put(this, configPath);
    }

    Map<ModCore, Path> configPaths = new HashMap<>();

    default Configuration getDefaultConfig() {
        return defaultConfigs.get(this);
    }

    default void setDefaultConfig(Configuration config) {
        defaultConfigs.put(this, config);
    }

    Map<ModCore, Configuration> defaultConfigs = new HashMap<>();

    default ModContainer getContainer() {
        return containers.get(this);
    }

    default void setContainer(ModContainer container) {
        containers.put(this, container);
    }

    Map<ModCore, ModContainer> containers = new HashMap<>();
}
