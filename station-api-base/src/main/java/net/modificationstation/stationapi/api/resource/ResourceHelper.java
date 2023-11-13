package net.modificationstation.stationapi.api.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.util.Namespace.MINECRAFT;

/**
 * @deprecated Use station-resource-loader-v0 instead.
 */
@Deprecated
public class ResourceHelper {
    @API
    public static final ResourceHelper
            ASSETS = new ResourceHelper("/assets"),
            DATA = new ResourceHelper("/data");

    @API
    public final String rootPath;

    @API
    public ResourceHelper(String rootPath) {
        this.rootPath = rootPath;
    }

    public boolean contains(String path) {
        String startsWith = rootPath + "/";
        return path.startsWith(startsWith);
    }

    @API
    public String toPath(Identifier identifier, String subPath, String extension) {
        identifier = subPath.isEmpty() ? identifier : identifier.withPrefixedPath(subPath + "/");
        return toPath(extension.isEmpty() ? identifier : identifier.withSuffixedPath("." + extension));
    }

    @API
    public String toPath(Identifier identifier) {
        return identifier.namespace == MINECRAFT && identifier.path.startsWith("/") ? identifier.path : rootPath + "/" + identifier.namespace + "/" + identifier.path;
    }

    @API
    public Identifier toId(String path, String subPath, String extension) {
        // TODO: look into improving this "/" thing
        if (!path.startsWith("/"))
            path = "/" + path;
        if (!contains(path))
            throw new IllegalArgumentException("The path \"" + path + "\" doesn't start with the current ResourceManager's root path \"" + rootPath + "\"!");
        if (!path.endsWith("." + extension))
            throw new IllegalArgumentException("The path \"" + path + "\" doesn't end with the specified file extension \"" + extension + "\"!");
        path = path.substring((rootPath + "/").length());
        int indexOfSubpath = path.indexOf("/");
        if (indexOfSubpath == -1)
            throw new IllegalArgumentException("The path \"" + path + "\" doesn't have a namespace!");
        Namespace namespace = Namespace.of(path.substring(0, indexOfSubpath));
        int indexOfId = indexOfSubpath + 1 + subPath.length();
        String subPathCheck = path.substring(indexOfSubpath + 1, indexOfId);
        if (!subPathCheck.equals(subPath))
            throw new IllegalArgumentException("The path \"" + path + "\" doesn't have a matching subpath \"" + subPath + "\" after namespace!");
        String id = path.substring(indexOfId + 1);
        return Identifier.of(namespace, id.substring(0, id.length() - extension.length() - 1));
    }

    public Set<URL> find(String path, Predicate<String> filter) {
        return FabricLoader.getInstance().getAllMods().stream().map(modContainer -> find(Namespace.of(modContainer), path, filter)).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @API
    public Set<URL> find(Namespace namespace, String path, Predicate<String> filter) {
        path = rootPath + "/" + namespace + "/" + path;
        if (ResourceHelper.class.getResource(path) != null)
            try {
                return new RecursiveReader(path, filter).read();
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        else
            return Collections.emptySet();
    }
}
