package net.modificationstation.stationapi.impl.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.registry.ModID;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

public class ModResources {

    public static final Path[] ROOT_PATHS = FabricLoader.getInstance().getAllMods().stream().filter(modContainer -> !ModID.MINECRAFT.equals(ModID.of(modContainer))).sorted(Comparator.<ModContainer>comparingInt(modContainer -> {
        ModMetadata data = modContainer.getMetadata();
        return data.containsCustomValue("station-resource-loader-v0:resource_priority") ? data.getCustomValue("station-resource-loader-v0:resource_priority").getAsNumber().intValue() : 0;
    }).reversed()).flatMap(modContainer -> modContainer.getRootPaths().stream()).toArray(Path[]::new);

    public static Optional<Path> getTopPath(String path) {
        path = path.substring(1); // remove the "/", paths don't like it
        for (Path rootPath : ROOT_PATHS) {
            Path resolve = rootPath.resolve(path);
            if (Files.exists(resolve)) return Optional.of(resolve);
        }
        return Optional.empty();
    }
}
