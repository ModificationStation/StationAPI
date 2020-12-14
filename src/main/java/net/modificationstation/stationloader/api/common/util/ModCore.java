package net.modificationstation.stationloader.api.common.util;

import net.modificationstation.stationloader.api.common.config.Configuration;
import net.modificationstation.stationloader.api.common.registry.ModID;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public interface ModCore {

    Map<ModCore, Logger> loggers = new HashMap<>();
    Map<ModCore, Path> configPaths = new HashMap<>();
    Map<ModCore, Configuration> defaultConfigs = new HashMap<>();
    Map<ModCore, ModID> modIDs = new HashMap<>();

    default Logger getLogger() {
        return loggers.get(this);
    }

    default void setLogger(Logger log) {
        loggers.put(this, log);
    }

    default Path getConfigPath() {
        return configPaths.get(this);
    }

    default void setConfigPath(Path configPath) {
        configPaths.put(this, configPath);
    }

    default Configuration getDefaultConfig() {
        return defaultConfigs.get(this);
    }

    default void setDefaultConfig(Configuration config) {
        defaultConfigs.put(this, config);
    }

    default ModID getModID() {
        return modIDs.get(this);
    }

    default void setModID(ModID modID) {
        modIDs.put(this, modID);
    }
}
