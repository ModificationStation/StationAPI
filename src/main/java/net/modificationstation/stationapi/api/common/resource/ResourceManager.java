package net.modificationstation.stationapi.api.common.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.common.registry.ModID;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ResourceManager {

    public static Set<URL> findResources(String path) {
        return findResources(path, null);
    }

    public static Set<URL> findResources(String path, Predicate<String> filter) {
        Set<URL> resources = new HashSet<>();
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> resources.addAll(findResources(ModID.of(modContainer), path, filter)));
        return resources;
    }

    public static Set<URL> findResources(ModID modID, String path) {
        return findResources(modID, path, null);
    }

    public static Set<URL> findResources(ModID modID, String path, Predicate<String> filter) {
        Set<URL> resources = new HashSet<>();
        String fullPath = "assets/" + modID;
        if (ResourceManager.class.getResource(fullPath) != null)
            try {
                resources.addAll(new RecursiveReader(fullPath, filter).read());
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        return resources;
    }
}
