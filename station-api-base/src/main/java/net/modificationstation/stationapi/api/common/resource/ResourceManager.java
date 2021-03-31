package net.modificationstation.stationapi.api.common.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.util.API;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;

public class ResourceManager {

    @API
    public static Set<URL> findResources(String path) {
        return findResources(path, null);
    }

    @API
    public static Set<URL> findResources(String path, Predicate<String> filter) {
        Set<URL> resources = new HashSet<>();
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> resources.addAll(findResources(ModID.of(modContainer), path, filter)));
        return resources;
    }

    @API
    public static Set<URL> findResources(ModID modID, String path) {
        return findResources(modID, path, null);
    }

    @API
    public static Set<URL> findResources(ModID modID, String path, Predicate<String> filter) {
        Set<URL> resources = new HashSet<>();
        String fullPath = "/assets/" + modID + "/" + path;
        if (ResourceManager.class.getResource(fullPath) != null)
            try {
                resources.addAll(new RecursiveReader(fullPath, filter).read());
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        return resources;
    }
}
