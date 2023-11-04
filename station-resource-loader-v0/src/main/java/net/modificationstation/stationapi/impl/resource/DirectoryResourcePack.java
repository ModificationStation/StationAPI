package net.modificationstation.stationapi.impl.resource;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.util.PathUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class DirectoryResourcePack extends AbstractFileResourcePack {
    private static final Joiner SEPARATOR_JOINER = Joiner.on("/");
    private final Path root;

    public DirectoryResourcePack(String name, Path root, boolean alwaysStable) {
        super(name, alwaysStable);
        this.root = root;
    }

    @Override
    @Nullable
    public InputSupplier<InputStream> openRoot(String ... segments) {
        PathUtil.validatePath(segments);
        Path path = PathUtil.getPath(this.root, List.of(segments));
        if (Files.exists(path)) {
            return InputSupplier.create(path);
        }
        return null;
    }

    public static boolean isValidPath(Path path) {
        return true;
    }

    @Override
    @Nullable
    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        Path path = this.root.resolve(type.getDirectory()).resolve(id.namespace.toString());
        return DirectoryResourcePack.open(id, path);
    }

    public static InputSupplier<InputStream> open(Identifier id, Path path) {
        return PathUtil.split(id.path).get().map(segments -> {
            Path path2 = PathUtil.getPath(path, segments);
            return DirectoryResourcePack.open(path2);
        }, result -> {
            LOGGER.error("Invalid path {}: {}", id, result.message());
            return null;
        });
    }

    @Nullable
    private static InputSupplier<InputStream> open(Path path) {
        if (Files.exists(path) && DirectoryResourcePack.isValidPath(path)) {
            return InputSupplier.create(path);
        }
        return null;
    }

    @Override
    public void findResources(ResourceType type, Namespace namespace, String prefix, ResourcePack.ResultConsumer consumer) {
        PathUtil.split(prefix).get().ifLeft(prefixSegments -> {
            Path path = this.root.resolve(type.getDirectory()).resolve(namespace.toString());
            DirectoryResourcePack.findResources(namespace, path, prefixSegments, consumer);
        }).ifRight(result -> LOGGER.error("Invalid path {}: {}", prefix, result.message()));
    }

    public static void findResources(Namespace namespace, Path path, List<String> prefixSegments, ResourcePack.ResultConsumer consumer) {
        findResources(namespace, path, prefixSegments, consumer, false);
    }

    public static void findResources(Namespace namespace, Path path, List<String> prefixSegments, ResultConsumer consumer, boolean atRoot) {
        Path path22 = PathUtil.getPath(path, prefixSegments);
        try (Stream<Path> stream2 = Files.find(path22, Integer.MAX_VALUE, (path2, attributes) -> attributes.isRegularFile())){
            stream2.forEach(foundPath -> {
                String string2 = SEPARATOR_JOINER.join(path.relativize(foundPath));
                if (atRoot) string2 = "/" + string2;
                Identifier identifier = Identifier.of(namespace, string2);
                consumer.accept(identifier, InputSupplier.create(foundPath));
            });
        } catch (NoSuchFileException ignored) {
        } catch (IOException iOException) {
            LOGGER.error("Failed to list path {}", path22, iOException);
        }
    }

    @Override
    public Set<Namespace> getNamespaces(ResourceType type) {
        HashSet<Namespace> set = Sets.newHashSet();
        Path path = this.root.resolve(type.getDirectory());
        try (DirectoryStream<Path> directoryStream2 = Files.newDirectoryStream(path)){
            for (Path path2 : directoryStream2) {
                String string = path2.getFileName().toString();
                if (string.equals(string.toLowerCase(Locale.ROOT))) {
                    set.add(Namespace.of(string));
                    continue;
                }
                LOGGER.warn("Ignored non-lowercase namespace: {} in {}", string, this.root);
            }
        } catch (NoSuchFileException ignored) {
        } catch (IOException iOException) {
            LOGGER.error("Failed to list path {}", path, iOException);
        }
        return set;
    }

    @Override
    public void close() {}
}

