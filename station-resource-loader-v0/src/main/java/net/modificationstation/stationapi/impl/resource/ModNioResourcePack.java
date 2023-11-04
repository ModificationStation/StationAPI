package net.modificationstation.stationapi.impl.resource;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import lombok.val;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourcePackActivationType;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.PathUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Pattern;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class ModNioResourcePack implements ModResourcePack {
    private static final Pattern RESOURCE_PACK_PATH = Pattern.compile("[a-z0-9-_.]+");
    private static final FileSystem DEFAULT_FS = FileSystems.getDefault();

    private final Identifier id;
    private final String name;
    private final ModMetadata modInfo;
    private final List<Path> basePaths;
    private final ResourceType type;
    private final AutoCloseable closer;
    private final ResourcePackActivationType activationType;
    private final Map<ResourceType, Set<Namespace>> namespaces;

    public static ModNioResourcePack create(Identifier id, String name, ModContainer mod, String subPath, ResourceType type, ResourcePackActivationType activationType) {
        List<Path> rootPaths = mod.getRootPaths();
        List<Path> paths;

        if (subPath == null) paths = rootPaths;
        else {
            paths = new ArrayList<>(rootPaths.size());

            for (Path path : rootPaths) {
                path = path.toAbsolutePath().normalize();
                Path childPath = path.resolve(subPath.replace("/", path.getFileSystem().getSeparator())).normalize();

                if (!childPath.startsWith(path) || !exists(childPath)) continue;

                paths.add(childPath);
            }
        }

        if (paths.isEmpty()) return null;

        //noinspection resource
        ModNioResourcePack ret = new ModNioResourcePack(id, name, mod.getMetadata(), paths, type, null, activationType);

        return ret.getNamespaces(type).isEmpty() ? null : ret;
    }

    private ModNioResourcePack(Identifier id, String name, ModMetadata modInfo, List<Path> paths, ResourceType type, AutoCloseable closer, ResourcePackActivationType activationType) {
        this.id = id;
        this.name = name;
        this.modInfo = modInfo;
        this.basePaths = paths;
        this.type = type;
        this.closer = closer;
        this.activationType = activationType;
        this.namespaces = readNamespaces(paths, modInfo.getId());
    }

    static Map<ResourceType, Set<Namespace>> readNamespaces(List<Path> paths, String modId) {
        Map<ResourceType, Set<Namespace>> ret = new EnumMap<>(ResourceType.class);

        for (ResourceType type : ResourceType.values()) {
            ReferenceSet<Namespace> namespaces = new ReferenceOpenHashSet<>();
            namespaces.add(Namespace.MINECRAFT);

            for (Path path : paths) {
                Path dir = path.resolve(type.getDirectory());
                if (!Files.isDirectory(dir)) continue;

                String separator = path.getFileSystem().getSeparator();

                try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
                    for (Path p : ds) {
                        if (!Files.isDirectory(p)) continue;

                        String s = p.getFileName().toString();
                        // s may contain trailing slashes, remove them
                        s = s.replace(separator, "");

                        if (!RESOURCE_PACK_PATH.matcher(s).matches()) {
                            LOGGER.warn("Fabric NioResourcePack: ignored invalid namespace: {} in mod ID {}", s, modId);
                            continue;
                        }

                        namespaces.add(Namespace.of(s));
                    }
                } catch (IOException e) {
                    LOGGER.warn("getNamespaces in mod " + modId + " failed!", e);
                }
            }

            ret.put(type, namespaces);
        }

        return ret;
    }

    private Path getPath(String filename) {
        if (hasAbsentNs(filename)) return null;

        for (Path basePath : basePaths) {
            Path childPath = basePath.resolve(filename.replace("/", basePath.getFileSystem().getSeparator())).toAbsolutePath().normalize();

            if (childPath.startsWith(basePath) && exists(childPath)) return childPath;
        }

        return null;
    }

    private static final String resPrefix = ResourceType.CLIENT_RESOURCES.getDirectory() + "/";
    private static final String dataPrefix = ResourceType.SERVER_DATA.getDirectory() + "/";

    private boolean hasAbsentNs(String filename) {
        int prefixLen;
        ResourceType type;

        if (filename.startsWith(resPrefix)) {
            prefixLen = resPrefix.length();
            type = ResourceType.CLIENT_RESOURCES;
        } else if (filename.startsWith(dataPrefix)) {
            prefixLen = dataPrefix.length();
            type = ResourceType.SERVER_DATA;
        } else return false;

        int nsEnd = filename.indexOf('/', prefixLen);
        if (nsEnd < 0) return false;

        return !namespaces.get(type).contains(Namespace.of(filename.substring(prefixLen, nsEnd)));
    }

    private InputSupplier<InputStream> openFile(String filename) {
        Path path = getPath(filename);

        if (path != null && Files.isRegularFile(path)) return () -> Files.newInputStream(path);

        if (ModResourcePackUtil.containsDefault(this.modInfo, filename))
            return () -> ModResourcePackUtil.openDefault(this.modInfo, this.type, filename);

        return null;
    }

    @Nullable
    @Override
    public InputSupplier<InputStream> openRoot(String... pathSegments) {
        PathUtil.validatePath(pathSegments);
        return this.openFile(String.join("/", pathSegments));
    }

    @Override
    @Nullable
    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        final Path path = getPath(getFilename(type, id));
        return path == null ? null : InputSupplier.create(path);
    }

    @Override
    public void findResources(ResourceType type, Namespace namespace, String path, ResultConsumer visitor) {
        val atRoot = path.startsWith("/");
        if (!atRoot && !namespaces.getOrDefault(type, Collections.emptySet()).contains(namespace)) return;
        for (Path basePath : basePaths) {
            String separator = basePath.getFileSystem().getSeparator();
            Path nsPath = atRoot ? basePath : basePath.resolve(type.getDirectory()).resolve(namespace.toString());
            Path searchPath = nsPath.resolve((atRoot ? path.substring(1) : path).replace("/", separator)).normalize();
            if (!exists(searchPath)) continue;
            try {
                Files.walkFileTree(searchPath, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        String filename = nsPath.relativize(file).toString().replace(separator, "/");
                        if (atRoot) filename = "/" + filename;
                        Identifier identifier = namespace.id(filename);
                        visitor.accept(identifier, InputSupplier.create(file));
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                LOGGER.warn("findResources at " + path + " in namespace " + namespace + ", mod " + modInfo.getId() + " failed!", e);
            }
        }
    }

    @Override
    public Set<Namespace> getNamespaces(ResourceType type) {
        return namespaces.getOrDefault(type, Collections.emptySet());
    }

    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        try (InputStream is = Objects.requireNonNull(openFile("pack.mcmeta")).get()) {
            return AbstractFileResourcePack.parseMetadata(metaReader, is);
        }
    }

    @Override
    public void close() {
        if (closer != null) try {
            closer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ModMetadata getFabricModMetadata() {
        return modInfo;
    }

    public ResourcePackActivationType getActivationType() {
        return this.activationType;
    }

    @Override
    public String getName() {
        return name;
    }

    public Identifier getId() {
        return id;
    }

    private static boolean exists(Path path) {
        // NIO Files.exists is notoriously slow when checking the file system
        return path.getFileSystem() == DEFAULT_FS ? path.toFile().exists() : Files.exists(path);
    }

    private static String getFilename(ResourceType type, Identifier id) {
        return String.format(Locale.ROOT, "%s/%s/%s", type.getDirectory(), id.namespace, id.path);
    }
}
