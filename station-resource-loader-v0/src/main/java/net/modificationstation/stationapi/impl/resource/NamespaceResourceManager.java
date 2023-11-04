package net.modificationstation.stationapi.impl.resource;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.resource.*;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadata;
import net.modificationstation.stationapi.api.util.PathUtil;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class NamespaceResourceManager implements ResourceManager {
    protected final List<FilterablePack> packList = Lists.newArrayList();
    public final ResourceType type;
    private final Namespace namespace;

    public NamespaceResourceManager(ResourceType type, Namespace namespace) {
        this.type = type;
        this.namespace = namespace;
    }

    public void addPack(ResourcePack pack) {
        addPack(pack.getName(), pack, null);
    }

    public void addPack(ResourcePack pack, Predicate<Identifier> filter) {
        addPack(pack.getName(), pack, filter);
    }

    public void addPack(String name, Predicate<Identifier> filter) {
        addPack(name, null, filter);
    }

    private void addPack(String name, @Nullable ResourcePack underlyingPack, @Nullable Predicate<Identifier> filter) {
        packList.add(new FilterablePack(name, underlyingPack, filter));
    }

    public Set<Namespace> getAllNamespaces() {
        return ImmutableSet.of(namespace);
    }

    private Function<ResourcePack, InputSupplier<InputStream>> createOpener(Identifier id) {
        if (id.namespace == Namespace.MINECRAFT && id.path.startsWith("/")) {
            final String[] segments = PathUtil.split(id.path.substring(1)).getOrThrow(false, LOGGER::error).toArray(String[]::new);
            return pack -> pack.openRoot(segments.clone());
        } else return pack -> pack.open(type, id);
    }

    @Override
    public Optional<Resource> getResource(Identifier id) {
        final Function<ResourcePack, InputSupplier<InputStream>> opener = createOpener(id);
        for(int i = packList.size() - 1; i >= 0; --i) {
            FilterablePack filterablePack = packList.get(i);
            ResourcePack resourcePack = filterablePack.underlying;
            if (resourcePack != null) {
                InputSupplier<InputStream> inputSupplier = opener.apply(resourcePack);
                if (inputSupplier != null) {
                    InputSupplier<ResourceMetadata> inputSupplier2 = createMetadataSupplier(id, i);
                    return Optional.of(createResource(resourcePack, id, inputSupplier, inputSupplier2));
                }
            }

            if (filterablePack.isFiltered(id)) {
                LOGGER.warn("Resource {} not found, but was filtered by pack {}", id, filterablePack.name);
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private static Resource createResource(ResourcePack pack, Identifier id, InputSupplier<InputStream> supplier, InputSupplier<ResourceMetadata> metadataSupplier) {
        return new Resource(pack, wrapForDebug(id, pack, supplier), metadataSupplier);
    }

    private static InputSupplier<InputStream> wrapForDebug(Identifier id, ResourcePack pack, InputSupplier<InputStream> supplier) {
        return LOGGER.isDebugEnabled() ? () -> new DebugInputStream(supplier.get(), id, pack.getName()) : supplier;
    }

    @Override
    public List<Resource> getAllResources(Identifier id) {
        Identifier identifier = getMetadataPath(id);
        final Function<ResourcePack, InputSupplier<InputStream>>
                opener = createOpener(id),
                metaOpener = createOpener(identifier);
        List<Resource> list = new ArrayList<>();
        boolean bl = false;
        String string = null;

        for(int i = packList.size() - 1; i >= 0; --i) {
            FilterablePack filterablePack = packList.get(i);
            ResourcePack resourcePack = filterablePack.underlying;
            if (resourcePack != null) {
                if (resourcePack instanceof GroupResourcePack group) {
                    group.appendResources(type, id, list);
                    continue;
                }
                InputSupplier<InputStream> inputSupplier = opener.apply(resourcePack);
                if (inputSupplier != null) {
                    InputSupplier<ResourceMetadata> inputSupplier2 = bl ? ResourceMetadata.NONE_SUPPLIER : (() -> {
                        InputSupplier<InputStream> inputSupplier1 = metaOpener.apply(resourcePack);
                        return inputSupplier1 != null ? loadMetadata(inputSupplier1) : ResourceMetadata.NONE;
                    });

                    list.add(new Resource(resourcePack, inputSupplier, inputSupplier2));
                }
            }

            if (filterablePack.isFiltered(id)) {
                string = filterablePack.name;
                break;
            }

            if (filterablePack.isFiltered(identifier)) bl = true;
        }

        if (list.isEmpty() && string != null)
            LOGGER.warn("Resource {} not found, but was filtered by pack {}", id, string);

        return Lists.reverse(list);
    }

    private static boolean isMcmeta(Identifier id) {
        return id.path.endsWith(".mcmeta");
    }

    private static Identifier getMetadataFileName(Identifier id) {
        return id.namespace.id(id.path.substring(0, id.path.length() - ".mcmeta".length()));
    }

    public static Identifier getMetadataPath(Identifier id) {
        return id.withSuffixedPath(".mcmeta");
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        record Result(ResourcePack pack, InputSupplier<InputStream> supplier, int packIndex) {}
        Map<Identifier, Result> map = new HashMap<>();
        Map<Identifier, Result> map2 = new HashMap<>();
        int i = packList.size();

        for(int j = 0; j < i; ++j) {
            FilterablePack filterablePack = packList.get(j);
            filterablePack.removeFiltered(map.keySet());
            filterablePack.removeFiltered(map2.keySet());
            ResourcePack resourcePack = filterablePack.underlying;
            if (resourcePack != null) {
                int finalJ = j;
                resourcePack.findResources(type, namespace, startingPath, (id, supplier) -> {
                    if (isMcmeta(id)) {
                        if (allowedPathPredicate.test(getMetadataFileName(id)))
                            map2.put(id, new Result(resourcePack, supplier, finalJ));
                    } else if (allowedPathPredicate.test(id)) map.put(id, new Result(resourcePack, supplier, finalJ));
                });
            }
        }

        Map<Identifier, Resource> map3 = Maps.newTreeMap();
        map.forEach((id, result) -> {
            Identifier identifier = getMetadataPath(id);
            Result result2 = map2.get(identifier);
            InputSupplier<ResourceMetadata> inputSupplier;
            if (result2 != null && result2.packIndex >= result.packIndex)
                inputSupplier = getMetadataSupplier(result2.supplier);
            else inputSupplier = ResourceMetadata.NONE_SUPPLIER;

            map3.put(id, createResource(result.pack, id, result.supplier, inputSupplier));
        });
        return map3;
    }

    private InputSupplier<ResourceMetadata> createMetadataSupplier(Identifier id, int index) {
        return () -> {
            Identifier identifier2 = getMetadataPath(id);
            final Function<ResourcePack, InputSupplier<InputStream>> opener = createOpener(identifier2);

            for(int j = packList.size() - 1; j >= index; --j) {
                FilterablePack filterablePack = packList.get(j);
                ResourcePack resourcePack = filterablePack.underlying;
                if (resourcePack != null) {
                    InputSupplier<InputStream> inputSupplier = opener.apply(resourcePack);
                    if (inputSupplier != null) return loadMetadata(inputSupplier);
                }

                if (filterablePack.isFiltered(identifier2)) break;
            }

            return ResourceMetadata.NONE;
        };
    }

    private static InputSupplier<ResourceMetadata> getMetadataSupplier(InputSupplier<InputStream> supplier) {
        return () -> loadMetadata(supplier);
    }

    public static ResourceMetadata loadMetadata(InputSupplier<InputStream> supplier) throws IOException {
        InputStream inputStream = supplier.get();

        ResourceMetadata var2;
        try {
            var2 = ResourceMetadata.create(inputStream);
        } catch (Throwable var5) {
            if (inputStream != null) try {
                inputStream.close();
            } catch (Throwable var4) {
                var5.addSuppressed(var4);
            }

            throw var5;
        }

        if (inputStream != null) inputStream.close();

        return var2;
    }

    private static void applyFilter(FilterablePack pack, Map<Identifier, EntryList> idToEntryList) {

        for (EntryList entryList : idToEntryList.values())
            if (pack.isFiltered(entryList.id)) entryList.fileSources.clear();
            else if (pack.isFiltered(entryList.metadataId())) entryList.metaSources.clear();

    }

    private void findAndAdd(FilterablePack pack, String startingPath, Predicate<Identifier> allowedPathPredicate, Map<Identifier, EntryList> idToEntryList) {
        ResourcePack resourcePack = pack.underlying;
        if (resourcePack != null)
            resourcePack.findResources(type, namespace, startingPath, (id, supplier) -> {
                if (isMcmeta(id)) {
                    Identifier identifier = getMetadataFileName(id);
                    if (!allowedPathPredicate.test(identifier)) return;

                    idToEntryList.computeIfAbsent(identifier, EntryList::new).metaSources.put(resourcePack, supplier);
                } else {
                    if (!allowedPathPredicate.test(id)) return;

                    idToEntryList.computeIfAbsent(id, EntryList::new).fileSources.add(new FileSource(resourcePack, supplier));
                }

            });
    }

    @Override
    public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        Map<Identifier, EntryList> map = Maps.newHashMap();
        for (FilterablePack filterablePack : packList) {
            NamespaceResourceManager.applyFilter(filterablePack, map);
            findAndAdd(filterablePack, startingPath, allowedPathPredicate, map);
        }
        Map<Identifier, List<Resource>> treeMap = Maps.newTreeMap();
        for (EntryList entryList : map.values()) {
            if (entryList.fileSources.isEmpty()) continue;
            ArrayList<Resource> list = new ArrayList<>();
            for (FileSource fileSource : entryList.fileSources) {
                ResourcePack resourcePack = fileSource.sourcePack;
                InputSupplier<InputStream> inputSupplier = entryList.metaSources.get(resourcePack);
                InputSupplier<ResourceMetadata> inputSupplier2 = inputSupplier != null ? NamespaceResourceManager.getMetadataSupplier(inputSupplier) : ResourceMetadata.NONE_SUPPLIER;
                list.add(NamespaceResourceManager.createResource(resourcePack, entryList.id, fileSource.supplier, inputSupplier2));
            }
            treeMap.put(entryList.id, list);
        }
        return treeMap;
    }

    public Stream<ResourcePack> streamResourcePacks() {
        return packList.stream().map((pack) -> pack.underlying).filter(Objects::nonNull);
    }

    @Override
    public Optional<ResourceType> getResourceType() {
        return Optional.of(type);
    }

    record FilterablePack(String name, @Nullable ResourcePack underlying, @Nullable Predicate<Identifier> filter) {
        public void removeFiltered(Collection<Identifier> ids) {
            if (filter != null) ids.removeIf(filter);
        }

        public boolean isFiltered(Identifier id) {
            return filter != null && filter.test(id);
        }
    }

    record EntryList(Identifier id, Identifier metadataId, List<FileSource> fileSources, Map<ResourcePack, InputSupplier<InputStream>> metaSources) {
        EntryList(Identifier id) {
            this(id, NamespaceResourceManager.getMetadataPath(id), new ArrayList<>(), new Object2ObjectArrayMap<>());
        }
    }

    record FileSource(ResourcePack sourcePack, InputSupplier<InputStream> supplier) { }

    private static class DebugInputStream extends FilterInputStream {
        private final Supplier<String> leakMessage;
        private boolean closed;

        public DebugInputStream(InputStream parent, Identifier id, String packName) {
            super(parent);
            Exception exception = new Exception("Stacktrace");
            leakMessage = () -> {
                StringWriter stringWriter = new StringWriter();
                exception.printStackTrace(new PrintWriter(stringWriter));
                return "Leaked resource: '" + id + "' loaded from pack: '" + packName + "'\n" + stringWriter;
            };
        }

        @Override
        public void close() throws IOException {
            super.close();
            closed = true;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void finalize() throws Throwable {
            if (!closed) LOGGER.warn("{}", leakMessage.get());

            super.finalize();
        }
    }
}
