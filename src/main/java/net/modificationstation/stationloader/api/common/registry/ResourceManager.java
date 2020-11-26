package net.modificationstation.stationloader.api.common.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationloader.impl.common.util.RecursiveReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ResourceManager {

    public static Set<Resource> findResources(String path) {
        return findResources(path, null);
    }

    public static Set<Resource> findResources(String path, Predicate<String> filter) {
        Set<Resource> resources = new HashSet<>();
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> resources.addAll(findResources(ModID.of(modContainer), path, filter)));
        return resources;
    }

    public static Set<Resource> findResources(ModID modID, String path) {
        return findResources(modID, path, null);
    }

    public static Set<Resource> findResources(ModID modID, String path, Predicate<String> filter) {
        Set<Resource> resources = new HashSet<>();
        String fullPath = "assets/" + modID;
        /*if (ResourceManager.class.getResource(fullPath) != null)
            try {
                new RecursiveReader(fullPath, filter).read().forEach();
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }*/
        return resources;
    }
}
