package net.modificationstation.stationapi.api.common;

import net.fabricmc.loader.api.ModContainer;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.util.HasHandler;
import net.modificationstation.stationapi.api.common.util.ModCore;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Set;

/**
 * StationAPI interface. Used to access StationAPI implementation side-independently.
 * @author mine_diver
 */
public interface StationAPI extends ModCore, HasHandler<StationAPI> {

    /**
     * Interface instance that holds the actual handler.
     */
    StationAPI INSTANCE = new StationAPI() {

        private StationAPI handler;

        @Override
        public void setHandler(StationAPI handler) {
            this.handler = handler;
        }

        @Override
        public Logger getLogger() {
            checkAccess(handler);
            return handler.getLogger();
        }

        @Override
        public void setLogger(Logger log) {
            checkAccess(handler);
            handler.setLogger(log);
        }

        @Override
        public File getConfigDir() {
            checkAccess(handler);
            return handler.getConfigDir();
        }

        @Override
        public void setConfigDir(File configPath) {
            checkAccess(handler);
            handler.setConfigDir(configPath);
        }

        @Override
        public Configuration getDefaultConfig() {
            checkAccess(handler);
            return handler.getDefaultConfig();
        }

        @Override
        public void setDefaultConfig(Configuration config) {
            checkAccess(handler);
            handler.setDefaultConfig(config);
        }

        @Override
        public ModID getModID() {
            checkAccess(handler);
            return handler.getModID();
        }

        @Override
        public void setModID(ModID modID) {
            checkAccess(handler);
            handler.setModID(modID);
        }

        @Override
        public void setup() {
            checkAccess(handler);
            handler.setup();
        }

        @Override
        public void setupMods() {
            checkAccess(handler);
            handler.setupMods();
        }

        @Override
        public Set<ModContainer> getModsToVerifyOnClient() {
            checkAccess(handler);
            return handler.getModsToVerifyOnClient();
        }
    };

    /**
     * Initial setup. Configures logger, entrypoints, and calls the rest of initialization sequence.
     */
    void setup();

    /**
     * Loads main entrypoints and scans mods assets, also invokes preInit, init and postInit events.
     */
    void setupMods();

    /**
     * Returns the set of mods that need client-side verification when the client joins server.
     * @return the set of mods that need client-side verification when the client joins server.
     */
    Set<ModContainer> getModsToVerifyOnClient();
}
