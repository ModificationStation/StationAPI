package net.modificationstation.stationloader.common.api;

import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

public interface StationLoader {

    @SuppressWarnings("deprecation")
    static StationLoader getInstance() {
        if (net.modificationstation.stationloader.common.StationLoader.INSTANCE == null)
            throw new RuntimeException("Accessed StationLoader too early!");
        else
            return net.modificationstation.stationloader.common.StationLoader.INSTANCE;
    }

    void setup() throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    void addMod(ModMetadata data, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, IOException;

    Collection<StationMod> getAllMods();

    StationMod getModInstance(Class<? extends StationMod> modClass);

    Logger getLogger();
}
