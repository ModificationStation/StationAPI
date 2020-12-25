package net.modificationstation.stationapi.api.common.util;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.api.common.registry.ModID;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public interface ModCore {

    Map<ModID, Logger> loggers = new HashMap<>();
    Map<ModID, File> configDirs = new HashMap<>();
    Map<ModID, Configuration> defaultConfigs = new HashMap<>();
    Map<ModCore, ModID> modIDs = new HashMap<>();

    static Logger getLogger(ModID modID) {
        return loggers.computeIfAbsent(modID, modID1 -> {
            String name = modID1.getName() + "|ModCore";
            Logger log = LogManager.getFormatterLogger();
            Configurator.setLevel(name, Level.INFO);
            return log;
        });
    }

    default Logger getLogger() {
        return getLogger(getModID());
    }

    default void setLogger(Logger log) {
        loggers.put(getModID(), log);
    }

    static File getConfigDir(ModID modID) {
        return configDirs.computeIfAbsent(modID, modID1 -> new File(FabricLoader.getInstance().getConfigDir() + File.separator + modID1));
    }

    default File getConfigDir() {
        return getConfigDir(getModID());
    }

    default void setConfigDir(File configPath) {
        configDirs.put(getModID(), configPath);
    }

    static Configuration getDefaultConfig(ModID modID) {
        return defaultConfigs.computeIfAbsent(modID, modID1 -> GeneralFactory.INSTANCE.newInst(Configuration.class, new File(getConfigDir(modID1) + File.separator + modID1 + ".cfg")));
    }

    default Configuration getDefaultConfig() {
        return getDefaultConfig(getModID());
    }

    default void setDefaultConfig(Configuration config) {
        defaultConfigs.put(getModID(), config);
    }

    default ModID getModID() {
        return modIDs.get(this);
    }

    default void setModID(ModID modID) {
        modIDs.put(this, modID);
    }
}
