package net.modificationstation.stationloader.api.common;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationloader.api.common.config.Configuration;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import net.modificationstation.stationloader.api.common.registry.ModID;
import net.modificationstation.stationloader.api.common.util.HasHandler;
import net.modificationstation.stationloader.api.common.util.ModCore;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

public interface StationLoader extends ModCore, HasHandler<StationLoader> {

    StationLoader INSTANCE = new StationLoader() {

        private StationLoader handler;

        @Override
        public void setHandler(StationLoader handler) {
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
        public Path getConfigPath() {
            checkAccess(handler);
            return handler.getConfigPath();
        }

        @Override
        public void setConfigPath(Path configPath) {
            checkAccess(handler);
            handler.setConfigPath(configPath);
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
        public void loadMods() {
            checkAccess(handler);
            handler.loadMods();
        }

        @Override
        public void addMod(EntrypointContainer<StationMod> stationModEntrypointContainer) {
            checkAccess(handler);
            handler.addMod(stationModEntrypointContainer);
        }

        @Override
        public void addModAssets(ModContainer modContainer) {
            checkAccess(handler);
            handler.addModAssets(modContainer);
        }

        @Override
        public Collection<ModContainer> getAllMods() {
            checkAccess(handler);
            return handler.getAllMods();
        }

        @Override
        public Set<StationMod> getAllModInstances() {
            checkAccess(handler);
            return handler.getAllModInstances();
        }

        @Override
        public Set<StationMod> getModInstances(ModContainer modContainer) {
            checkAccess(handler);
            return handler.getModInstances(modContainer);
        }

        @Override
        public Set<ModContainer> getModsToVerifyOnClient() {
            checkAccess(handler);
            return handler.getModsToVerifyOnClient();
        }
    };

    void setup();

    void loadMods();

    void addMod(EntrypointContainer<StationMod> stationModEntrypointContainer);

    void addModAssets(ModContainer modContainer);

    Collection<ModContainer> getAllMods();

    Set<StationMod> getAllModInstances();

    Set<StationMod> getModInstances(ModContainer modContainer);

    Set<ModContainer> getModsToVerifyOnClient();
}
