package net.modificationstation.stationapi.api.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.API;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ResourceManager {

    @API
    public static final ResourceManager
            ASSETS = new ResourceManager("/assets"),
            DATA = new ResourceManager("/data");

    @API
    public final String rootPath;

    @API
    public ResourceManager(String rootPath) {
        this.rootPath = rootPath;
    }

    @API
    public String toPath(Identifier identifier, String subPath, String extension) {
        identifier = subPath.isEmpty() ? identifier : identifier.prepend(subPath + "/");
        return toPath(extension.isEmpty() ? identifier : identifier.append("." + extension));
    }

    @API
    public String toPath(Identifier identifier) {
        return rootPath + "/" + identifier.modID + "/" + identifier.id;
    }

//    public Identifier toIdentifier(String path, String subPath) {
//        String startsWith = rootPath + "/";
//        if (!path.startsWith(startsWith))
//            throw new IllegalArgumentException("The path \"" + path + "\" doesn't start with the current ResourceManager's root path \"" + rootPath + "\"!");
//        path = path.substring(startsWith.length());
//        int indexOfSubpath = path.indexOf("/");
//        if (indexOfSubpath == -1)
//            throw new IllegalArgumentException("The path \"" + path + "\" doesn't have a modid!");
//        ModID modid = ModID.of(path.substring(0, indexOfSubpath));
//        int indexOfId = indexOfSubpath + 1 + subPath.length();
//        String subPathCheck = path.substring(indexOfSubpath + 1, indexOfId);
//        if (!subPathCheck.equals(subPath))
//            throw new IllegalArgumentException("The path \"" + path + "\" doesn't have a matching subpath \"" + subPath + "\" after modid!");
//        String id = path.substring(indexOfId + 1);
//
//    }

    public Set<URL> find(String path, Predicate<String> filter) {
        return FabricLoader.getInstance().getAllMods().stream().map(modContainer -> find(ModID.of(modContainer), path, filter)).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @API
    public Set<URL> find(ModID modID, String path, Predicate<String> filter) {
        path = rootPath + "/" + modID + "/" + path;
        if (ResourceManager.class.getResource(path) != null)
            try {
                return new RecursiveReader(path, filter).read();
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        else
            return Collections.emptySet();
    }

    @Deprecated
    public static String parsePath(Identifier identifier, String path, String extension) {
        return ASSETS.toPath(identifier, path, extension);
    }

    @Deprecated
    public static Set<URL> findResources(String path) {
        return ASSETS.find(path, Filters.ALL);
    }

    @Deprecated
    public static Set<URL> findResources(String path, Predicate<String> filter) {
        return ASSETS.find(path, filter);
    }

    @Deprecated
    public static Set<URL> findResources(ModID modID, String path) {
        return ASSETS.find(modID, path, Filters.ALL);
    }

    @Deprecated
    public static Set<URL> findResources(ModID modID, String path, Predicate<String> filter) {
        return ASSETS.find(modID, path, filter);
    }
}
