package net.modificationstation.stationloader.api.common;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.metadata.LoaderModMetadata;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

public interface StationLoader {

    StationLoader INSTANCE = getInstance();

    @Deprecated
    static StationLoader getInstance() {
        if (INSTANCE == null) {
            try {
                Optional<ModContainer> modContainerOptional =  FabricLoader.getInstance().getModContainer("stationloader");
                if (modContainerOptional.isPresent()) {
                    Class<?> slClass = Class.forName(((LoaderModMetadata) modContainerOptional.get().getMetadata()).getEntrypoints("stationloader_" + FabricLoader.getInstance().getEnvironmentType().name().toLowerCase()).get(0).getValue());
                    if (StationLoader.class.isAssignableFrom(slClass))
                        return (StationLoader) slClass.newInstance();
                    else
                        throw new RuntimeException("Corrupted StationLoader class!");
                } else
                    throw new RuntimeException("Corrupted fabric.mod.json!");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else
            return INSTANCE;
    }

    void setup() throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    void addMod(ModMetadata data, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, IOException;

    Collection<StationMod> getAllMods();

    Collection<Class<? extends StationMod>> getAllModsClasses();

    StationMod getModInstance(Class<? extends StationMod> modClass);

    Logger getLogger();
}
