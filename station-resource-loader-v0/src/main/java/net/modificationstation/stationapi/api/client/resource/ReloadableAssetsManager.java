package net.modificationstation.stationapi.api.client.resource;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.*;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadata;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.ModResources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReloadableAssetsManager implements ResourceManager {

    public static final ReloadableAssetsManager INSTANCE = Util.make(new ReloadableAssetsManager(), resourceManager -> StationAPI.EVENT_BUS.post(AssetsResourceReloaderRegisterEvent.builder().resourceManager(resourceManager).build()));
    private static final Joiner SEPARATOR_JOINER = Joiner.on("/");

    private TexturePack activePack;
    private final ReferenceList<ResourceReloader> reloaders = new ReferenceArrayList<>();

    @Override
    public Optional<Resource> getResource(Identifier id) {
        String path = ResourceHelper.ASSETS.toPath(id);
        try (InputStream stream = activePack.getResourceAsStream(path)) {
            if (stream != null) {
                String metaPath = path + ".mcmeta";
                try (InputStream metaStream = activePack.getResourceAsStream(metaPath)) {
                    return Optional.of(new Resource(() -> activePack.getResourceAsStream(path), metaStream == null ? () -> ResourceMetadata.NONE : () -> {
                        InputStream newMetaStream = activePack.getResourceAsStream(metaPath);
                        ResourceMetadata meta;
                        try {
                            meta = ResourceMetadata.create(newMetaStream);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return meta;
                    }));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> getAllResources(Identifier id) {
        Identifier metaId = getMetadataPath(id);
        ReferenceList<Resource> list = new ReferenceArrayList<>();
        boolean skipMeta = false;
        for (Path modRoot : ModResources.ROOT_PATHS) {
            Path modPath = modRoot.resolve(ResourceHelper.ASSETS.toPath(id).substring(1));
            if (Files.exists(modPath)) {
                Path metaPath = modRoot.resolve(ResourceHelper.ASSETS.toPath(metaId).substring(1));
                list.add(new Resource(InputSupplier.create(modPath), skipMeta || !Files.exists(metaPath) ? ResourceMetadata.NONE_SUPPLIER : () -> ResourceMetadata.create(Files.newInputStream(metaPath))));
            }
            skipMeta = true;
        }
        return Lists.reverse(list);
    }

    private static Identifier getMetadataFileName(Identifier id) {
        return id.modID.id(id.id.substring(0, id.id.length() - ".mcmeta".length()));
    }

    private static Identifier getMetadataPath(Identifier id) {
        return id.append(".mcmeta");
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        record Result(InputSupplier<InputStream> supplier, int packIndex) {}
        Reference2ReferenceMap<Identifier, Result> resources = new Reference2ReferenceOpenHashMap<>();
        Reference2ReferenceMap<Identifier, Result> metas = new Reference2ReferenceOpenHashMap<>();
        Path[] rootPaths = ModResources.ROOT_PATHS;
        for (int i = 0, rootPathsLength = rootPaths.length; i < rootPathsLength; i++) {
            Path modRoot = rootPaths[i];
            int index = i;
            findResources(ResourceType.CLIENT_RESOURCES, modRoot, startingPath, (id, supplier) -> {
                if (isMcmeta(id)) {
                    if (allowedPathPredicate.test(getMetadataFileName(id)))
                        metas.put(id, new Result(supplier, index));
                } else if (allowedPathPredicate.test(id)) resources.put(id, new Result(supplier, index));
            });
        }
        Object2ReferenceSortedMap<Identifier, Resource> topResources = new Object2ReferenceRBTreeMap<>();
        resources.forEach((id, result) -> {
            Identifier identifier = getMetadataPath(id);
            Result metaResult = metas.get(identifier);
            InputSupplier<ResourceMetadata> inputSupplier = metaResult != null && metaResult.packIndex >= result.packIndex ? getMetadataSupplier(metaResult.supplier) : ResourceMetadata.NONE_SUPPLIER;
            topResources.put(id, new Resource(result.supplier, inputSupplier));
        });
        return topResources;
    }

    private static void findResources(ResourceType type, Path root, String prefix, BiConsumer<Identifier, InputSupplier<InputStream>> consumer) {
        Path resType = root.resolve(type.getDirectory());
        try (Stream<Path> namespaces = Files.find(resType, 0, (path, basicFileAttributes) -> basicFileAttributes.isDirectory())) {
            namespaces.forEach(namespace -> {
                try (Stream<Path> resources = Files.find(namespace.resolve(prefix), Integer.MAX_VALUE, (path, basicFileAttributes) -> basicFileAttributes.isRegularFile())) {
                    resources.forEach(resource -> consumer.accept(ModID.of(SEPARATOR_JOINER.join(resType.relativize(namespace))).id(SEPARATOR_JOINER.join(namespace.relativize(resource))), InputSupplier.create(resource)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isMcmeta(Identifier id) {
        return id.id.endsWith(".mcmeta");
    }

    private static InputSupplier<ResourceMetadata> getMetadataSupplier(InputSupplier<InputStream> supplier) {
        return () -> loadMetadata(supplier);
    }

    private static ResourceMetadata loadMetadata(InputSupplier<InputStream> supplier) throws IOException {
        try (InputStream inputStream = supplier.get()) {
            return ResourceMetadata.create(inputStream);
        }
    }

    @Override
    public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        Reference2ReferenceMap<Identifier, EntryList> map = new Reference2ReferenceOpenHashMap<>();
        for (Path root : ModResources.ROOT_PATHS) findAndAdd(root, startingPath, allowedPathPredicate, map);
        Object2ReferenceSortedMap<Identifier, List<Resource>> treeMap = new Object2ReferenceRBTreeMap<>();
        for (EntryList entryList : map.values()) {
            if (entryList.fileSources.isEmpty()) continue;
            ReferenceList<Resource> list = new ReferenceArrayList<>();
            for (FileSource fileSource : entryList.fileSources) {
                Path sourcePath = fileSource.sourcePath;
                InputSupplier<InputStream> inputSupplier = entryList.metaSources.get(sourcePath);
                InputSupplier<ResourceMetadata> inputSupplier2 = inputSupplier != null ? getMetadataSupplier(inputSupplier) : ResourceMetadata.NONE_SUPPLIER;
                list.add(new Resource(fileSource.supplier, inputSupplier2));
            }
            treeMap.put(entryList.id, list);
        }
        return treeMap;
    }

    record EntryList(Identifier id, Identifier metadataId, List<FileSource> fileSources, Map<Path, InputSupplier<InputStream>> metaSources) {
        EntryList(Identifier id) {
            this(id, getMetadataPath(id), new ArrayList<>(), new Object2ReferenceOpenHashMap<>());
        }
    }

    record FileSource(Path sourcePath, InputSupplier<InputStream> supplier) {}

    private static void findAndAdd(Path root, String startingPath, Predicate<Identifier> allowedPathPredicate, Map<Identifier, EntryList> idToEntryList) {
        findResources(ResourceType.CLIENT_RESOURCES, root, startingPath, (id, supplier) -> {
            if (isMcmeta(id)) {
                Identifier identifier = getMetadataFileName(id);
                if (!allowedPathPredicate.test(identifier)) return;
                idToEntryList.computeIfAbsent(identifier, EntryList::new).metaSources.put(root, supplier);
            } else {
                if (!allowedPathPredicate.test(id)) return;
                idToEntryList.computeIfAbsent(id, EntryList::new).fileSources.add(new FileSource(root, supplier));
            }
        });
    }

    /**
     * Registers a reloader to all future reloads on this resource
     * manager.
     */
    public void registerReloader(ResourceReloader reloader) {
        this.reloaders.add(reloader);
    }

    /**
     * Swaps the active resource manager with another one backed by the given
     * {@code packs} and start a {@linkplain SimpleResourceReload#start reload}.
     */
    public ResourceReload reload(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, TexturePack pack) {
        LOGGER.info("Reloading ResourceManager: {}", pack.name);
        activePack = pack;
        return SimpleResourceReload.start(this, this.reloaders, prepareExecutor, applyExecutor, initialStage, LOGGER.isDebugEnabled());
    }
}
