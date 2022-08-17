package net.modificationstation.stationapi.api.resource;

import com.google.common.collect.ImmutableMap;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.exception.MissingModException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class DefaultResourcePack {
    public static final Map<ResourceType, Path> TYPE_TO_FILE_SYSTEM = Util.make(() -> {
        synchronized (DefaultResourcePack.class) {
            ImmutableMap.Builder<ResourceType, Path> builder = ImmutableMap.builder();
            for (ResourceType resourceType : ResourceType.values()) {
                String string = "/" + resourceType.getDirectory() + "/.mcassetsroot";
                URL uRL = DefaultResourcePack.class.getResource(string);
                if (uRL == null) {
                    LOGGER.error("File {} does not exist in classpath", string);
                    continue;
                }
                try {
                    URI uRI = uRL.toURI();
                    String string2 = uRI.getScheme();
                    if (!"jar".equals(string2) && !"file".equals(string2)) {
                        LOGGER.warn("Assets URL '{}' uses unexpected schema", uRI);
                    }
                    Path path = DefaultResourcePack.getPath(uRI);
                    builder.put(resourceType, path.getParent());
                }
                catch (Exception exception) {
                    LOGGER.error("Couldn't resolve path to vanilla assets", exception);
                }
            }
            return builder.build();
        }
    });

    public static final Pattern RESOURCE_PACK_PATH = Pattern.compile("[a-z0-9-_.]+");

    public static Path getPath(URI uri) throws IOException {
        try {
            //noinspection DuplicateExpressions
            return Paths.get(uri);
        } catch (FileSystemNotFoundException ignored) {
        } catch (Throwable throwable) {
            LOGGER.warn("Unable to get path for: {}", uri, throwable);
        }
        try {
            //noinspection resource
            FileSystems.newFileSystem(uri, Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException ignored) {
        }
        //noinspection DuplicateExpressions
        return Paths.get(uri);
    }

    public static void collectIdentifiers(Collection<Identifier> results, String namespace, Path root, String prefix, Predicate<Identifier> allowedPathPredicate) throws IOException {
        Path path2 = root.resolve(namespace);
        try (Stream<Path> stream = Files.walk(path2.resolve(prefix))){
            stream.filter(path -> !path.endsWith(".mcmeta") && Files.isRegularFile(path)).<Identifier>mapMulti((path, consumer) -> {
                String string2 = path2.relativize(path).toString().replaceAll("\\\\", "/");
                try {
                    Identifier identifier = ModID.of(namespace).id(string2);
                    consumer.accept(identifier);
                } catch (MissingModException e) {
                    Util.error(String.format(Locale.ROOT, "Invalid path in datapack: %s:%s, ignoring", namespace, string2));
                }
            }).filter(allowedPathPredicate).forEach(results::add);
        }
    }

    public static String getPath(ResourceType type, Identifier id) {
        return "/" + type.getDirectory() + "/" + id.modID + "/" + id.id;
    }

    public static boolean isValidUrl(String fileName, @Nullable URL url) throws IOException {
        return url != null && (url.getProtocol().equals("jar") || DirectoryResourcePack.isValidPath(new File(url.getFile()), fileName));
    }
}
