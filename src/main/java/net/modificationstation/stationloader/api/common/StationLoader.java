package net.modificationstation.stationloader.api.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.metadata.LoaderModMetadata;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import net.modificationstation.stationloader.api.common.util.ModCore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public interface StationLoader extends ModCore {

    StationLoader INSTANCE = ((Supplier<StationLoader>) () -> {
        try {
            Optional<ModContainer> modContainerOptional =  FabricLoader.getInstance().getModContainer("stationloader");
            if (modContainerOptional.isPresent()) {
                ModMetadata data = modContainerOptional.get().getMetadata();
                if (data instanceof LoaderModMetadata) {
                    Class<?> clazz = Class.forName(((LoaderModMetadata) data).getEntrypoints("stationloader_" + FabricLoader.getInstance().getEnvironmentType().name().toLowerCase()).get(0).getValue());
                    if (StationLoader.class.isAssignableFrom(clazz)) {
                        Class<? extends StationLoader> slClass = clazz.asSubclass(StationLoader.class);
                        StationLoader stationLoader = slClass.newInstance();
                        stationLoader.setData(data);
                        return stationLoader;
                    } else
                        throw new RuntimeException("Corrupted StationLoader class!");
                } else
                    throw new RuntimeException("Corrupted fabric.mod.json!");
            } else
                throw new RuntimeException("Absent fabric.mod.json! (Impossible?)");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }).get();

    void setup() throws IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, URISyntaxException;

    void addModAssets(ModMetadata data) throws IOException, URISyntaxException;

    void addMod(ModMetadata data, EnvType envType, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, URISyntaxException;

    Collection<ModMetadata> getAllStationMods();

    Collection<Class<? extends StationMod>> getAllStationModsClasses();

    Collection<StationMod> getAllStationModInstances();

    Collection<Class<? extends StationMod>> getStationModClasses(ModMetadata data);

    StationMod getModInstance(Class<? extends StationMod> modClass);

    EnvType getModSide(ModMetadata data);
}
