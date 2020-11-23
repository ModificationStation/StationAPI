package net.modificationstation.stationloader.api.common;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.metadata.LoaderModMetadata;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import net.modificationstation.stationloader.api.common.util.ModCore;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public interface StationLoader extends ModCore {

    StationLoader INSTANCE = ((Supplier<StationLoader>) () -> {
        try {
            Optional<ModContainer> modContainerOptional =  FabricLoader.getInstance().getModContainer("stationloader");
            if (modContainerOptional.isPresent()) {
                ModContainer modContainer = modContainerOptional.get();
                ModMetadata data = modContainer.getMetadata();
                if (data instanceof LoaderModMetadata) {
                    Class<?> clazz = Class.forName(((LoaderModMetadata) data).getEntrypoints("stationloader_" + FabricLoader.getInstance().getEnvironmentType().name().toLowerCase()).get(0).getValue());
                    if (StationLoader.class.isAssignableFrom(clazz)) {
                        Class<? extends StationLoader> slClass = clazz.asSubclass(StationLoader.class);
                        StationLoader stationLoader = slClass.newInstance();
                        stationLoader.setContainer(modContainer);
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

    void setup();

    void loadMods();

    void addMod(EntrypointContainer<StationMod> stationModEntrypointContainer);

    void addModAssets(ModContainer modContainer);

    Collection<ModContainer> getAllMods();

    Set<StationMod> getAllModInstances();

    Set<StationMod> getModInstances(ModContainer modContainer);

    Set<ModContainer> getModsToVerifyOnClient();
}
