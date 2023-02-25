package net.modificationstation.stationapi.api.registry;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.dynamic.EntryLoader;
import net.modificationstation.stationapi.api.util.dynamic.RegistryLoader;
import net.modificationstation.stationapi.api.util.dynamic.RegistryOps;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * A manager of dynamic registries. It allows users to access non-hardcoded
 * registries reliably.
 * 
 * <p>Each minecraft server has a dynamic registry manager for file-loaded
 * registries, while each client play network handler has a dynamic registry
 * manager for server-sent dynamic registries.
 * 
 * <p>The {@link DynamicRegistryManager.ImmutableImpl}
 * class serves as an immutable implementation of any particular collection
 * or configuration of dynamic registries.
 */
public interface DynamicRegistryManager {
    Map<RegistryKey<? extends Registry<?>>, Info<?>> INFOS = Util.make(() -> {
        ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, Info<?>> builder = ImmutableMap.builder();
        return builder.build();
    });
    Codec<DynamicRegistryManager> CODEC = DynamicRegistryManager.createCodec();
    Supplier<Immutable> BUILTIN = Suppliers.memoize(() -> DynamicRegistryManager.createAndLoad().toImmutable());

    /**
     * Retrieves a registry optionally from this manager.
     */
    <E> Optional<Registry<E>> getOptionalManaged(RegistryKey<? extends Registry<? extends E>> var1);

    /**
     * Retrieves a registry from this manager,
     * or throws an exception when the registry does not exist.
     * 
     * @throws IllegalStateException if the registry does not exist
     */
    default <E> Registry<E> getManaged(RegistryKey<? extends Registry<? extends E>> key) {
        return this.getOptionalManaged(key).orElseThrow(() -> new IllegalStateException("Missing registry: " + key));
    }

    default <E> Optional<? extends Registry<E>> getOptional(RegistryKey<? extends Registry<? extends E>> key) {
        Optional<Registry<E>> optional = this.getOptionalManaged(key);
        if (optional.isPresent()) {
            return optional;
        }
        //noinspection unchecked
        return (Optional<? extends Registry<E>>) Registry.REGISTRIES.getOrEmpty(key.getValue());
    }

    /**
     * Retrieves a registry from this manager or {@link Registry#REGISTRIES},
     * or throws an exception when the registry does not exist.
     * 
     * @throws IllegalStateException if the registry does not exist
     */
    default <E> Registry<E> get(RegistryKey<? extends Registry<? extends E>> key) {
        return this.getOptional(key).orElseThrow(() -> new IllegalStateException("Missing registry: " + key));
    }

    private static <E> void register(ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, Info<?>> infosBuilder, RegistryKey<? extends Registry<E>> registryRef, Codec<E> entryCodec) {
        infosBuilder.put(registryRef, new Info<E>(registryRef, entryCodec, null));
    }

    private static <E> void register(ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, Info<?>> infosBuilder, RegistryKey<? extends Registry<E>> registryRef, Codec<E> entryCodec, Codec<E> networkEntryCodec) {
        infosBuilder.put(registryRef, new Info<E>(registryRef, entryCodec, networkEntryCodec));
    }

    static Iterable<Info<?>> getInfos() {
        return INFOS.values();
    }

    Stream<Entry<?>> streamManagedRegistries();

    private static Stream<Entry<Object>> streamStaticRegistries() {
        return Registry.REGISTRIES.streamEntries().map(Entry::of);
    }

    default Stream<Entry<?>> streamAllRegistries() {
        return Stream.concat(this.streamManagedRegistries(), DynamicRegistryManager.streamStaticRegistries());
    }

    default Stream<Entry<?>> streamSyncedRegistries() {
        return Stream.concat(this.streamSyncedManagedRegistries(), DynamicRegistryManager.streamStaticRegistries());
    }

    private static <E> Codec<DynamicRegistryManager> createCodec() {
        Codec<RegistryKey<? extends Registry<E>>> codec = Identifier.CODEC.xmap(RegistryKey::ofRegistry, RegistryKey::getValue);
        Codec<Registry<E>> codec2 = codec.partialDispatch("type", registry -> DataResult.success(registry.getKey()), registryRef -> DynamicRegistryManager.getNetworkEntryCodec(registryRef).map(codec3 -> RegistryCodecs.createRegistryCodec(registryRef, Lifecycle.experimental(), codec3)));
        UnboundedMapCodec<RegistryKey<? extends Registry<E>>, Registry<E>> unboundedMapCodec = Codec.unboundedMap(codec, codec2);
        return DynamicRegistryManager.createCodec(unboundedMapCodec);
    }

    private static <K extends RegistryKey<? extends Registry<?>>, V extends Registry<?>> Codec<DynamicRegistryManager> createCodec(UnboundedMapCodec<K, V> originalCodec) {
        //noinspection unchecked
        return originalCodec.xmap(ImmutableImpl::new, dynamicRegistryManager -> dynamicRegistryManager.streamSyncedManagedRegistries().collect(ImmutableMap.toImmutableMap(entry -> (K) entry.key(), entry -> (V) entry.value())));
    }

    private Stream<Entry<?>> streamSyncedManagedRegistries() {
        return this.streamManagedRegistries().filter(entry -> INFOS.get(entry.key).isSynced());
    }

    private static <E> DataResult<? extends Codec<E>> getNetworkEntryCodec(RegistryKey<? extends Registry<E>> registryKey) {
        //noinspection unchecked
        return Optional.ofNullable(INFOS.get(registryKey)).map(info -> (Codec<E>) info.networkEntryCodec()).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown or not serializable registry: " + registryKey));
    }

    private static Map<RegistryKey<? extends Registry<?>>, ? extends MutableRegistry<?>> createMutableRegistries() {
        return INFOS.keySet().stream().collect(Collectors.toMap(Function.identity(), DynamicRegistryManager::createSimpleRegistry));
    }

    private static Mutable createMutableRegistryManager() {
        return new MutableImpl(DynamicRegistryManager.createMutableRegistries());
    }

    static Immutable of(final Registry<? extends Registry<?>> registries) {
        return new Immutable(){

            public <T> Optional<Registry<T>> getOptionalManaged(RegistryKey<? extends Registry<? extends T>> key) {
                //noinspection unchecked
                return ((Registry<Registry<T>>) registries).getOrEmpty((RegistryKey<Registry<T>>) key);
            }

            @Override
            public Stream<Entry<?>> streamManagedRegistries() {
                return registries.getEntrySet().stream().map(Entry::of);
            }
        };
    }

    static Mutable createAndLoad() {
        Mutable mutable = DynamicRegistryManager.createMutableRegistryManager();
        EntryLoader.Impl impl = new EntryLoader.Impl();
        for (Map.Entry<RegistryKey<? extends Registry<?>>, Info<?>> entry : INFOS.entrySet()) {
            DynamicRegistryManager.addEntriesToLoad(impl, entry.getValue());
        }
        RegistryOps.ofLoaded(JsonOps.INSTANCE, mutable, impl);
        return mutable;
    }

    private static <E> void addEntriesToLoad(EntryLoader.Impl entryLoader, Info<E> info) {
        RegistryKey<? extends Registry<E>> registryKey = info.registry();
        Registry<E> registry = BuiltinRegistries.DYNAMIC_REGISTRY_MANAGER.get(registryKey);
        for (Map.Entry<RegistryKey<E>, E> entry : registry.getEntrySet()) {
            RegistryKey<E> registryKey2 = entry.getKey();
            E object = entry.getValue();
            entryLoader.add(BuiltinRegistries.DYNAMIC_REGISTRY_MANAGER, registryKey2, info.entryCodec(), registry.getRawId(object), object, registry.getEntryLifecycle(object));
        }
    }

    /**
     * Loads a dynamic registry manager from the resource manager's data files.
     */
    static void load(Mutable dynamicRegistryManager, DynamicOps<JsonElement> ops, RegistryLoader registryLoader) {
        RegistryLoader.LoaderAccess loaderAccess = registryLoader.createAccess(dynamicRegistryManager);
        for (Info<?> info : INFOS.values()) {
            DynamicRegistryManager.load(ops, loaderAccess, info);
        }
    }

    /**
     * Loads elements from the {@code ops} into the registry specified by {@code
     * info} within the {@code manager}. Note that the resource manager instance
     * is kept within the {@code ops}.
     */
    private static <E> void load(DynamicOps<JsonElement> ops, RegistryLoader.LoaderAccess loaderAccess, Info<E> info) {
        DataResult<? extends Registry<E>> dataResult = loaderAccess.load(info.registry(), info.entryCodec(), ops);
        dataResult.error().ifPresent(partialResult -> {
            throw new JsonParseException("Error loading registry data: " + partialResult.message());
        });
    }

    static DynamicRegistryManager createDynamicRegistryManager(Dynamic<?> dynamic) {
        return new ImmutableImpl(INFOS.keySet().stream().collect(Collectors.toMap(Function.identity(), registryRef -> DynamicRegistryManager.createRegistry(registryRef, dynamic))));
    }

    static <E> Registry<E> createRegistry(RegistryKey<? extends Registry<? extends E>> registryRef, Dynamic<?> dynamic) {
        return RegistryOps.createRegistryCodec(registryRef).codec().parse(dynamic).resultOrPartial(Util.addPrefix(registryRef + " registry: ", LOGGER::error)).orElseThrow(() -> new IllegalStateException("Failed to get " + registryRef + " registry"));
    }

    static <E> MutableRegistry<E> createSimpleRegistry(RegistryKey<? extends Registry<?>> registryRef) {
        //noinspection unchecked
        return new SimpleRegistry<>((RegistryKey<? extends Registry<E>>) registryRef, Lifecycle.stable(), null);
    }

    default Immutable toImmutable() {
        return new ImmutableImpl(this.streamManagedRegistries().map(Entry::freeze));
    }

    default Lifecycle getRegistryLifecycle() {
        return this.streamManagedRegistries().map(entry -> entry.value.getLifecycle()).reduce(Lifecycle.stable(), Lifecycle::add);
    }

    record Info<E>(RegistryKey<? extends Registry<E>> registry, Codec<E> entryCodec, @Nullable Codec<E> networkEntryCodec) {
        public boolean isSynced() {
            return this.networkEntryCodec != null;
        }
    }

    final class MutableImpl
    implements Mutable {
        private final Map<? extends RegistryKey<? extends Registry<?>>, ? extends MutableRegistry<?>> mutableRegistries;

        MutableImpl(Map<? extends RegistryKey<? extends Registry<?>>, ? extends MutableRegistry<?>> mutableRegistries) {
            this.mutableRegistries = mutableRegistries;
        }

        @Override
        public <E> Optional<Registry<E>> getOptionalManaged(RegistryKey<? extends Registry<? extends E>> key) {
            //noinspection unchecked
            return Optional.ofNullable(this.mutableRegistries.get(key)).map(registry -> (Registry<E>) registry);
        }

        @Override
        public <E> Optional<MutableRegistry<E>> getOptionalMutable(RegistryKey<? extends Registry<? extends E>> key) {
            //noinspection unchecked
            return Optional.ofNullable(this.mutableRegistries.get(key)).map(registry -> (MutableRegistry<E>) registry);
        }

        @Override
        public Stream<Entry<?>> streamManagedRegistries() {
            return this.mutableRegistries.entrySet().stream().map(Entry::of);
        }
    }

    interface Mutable
    extends DynamicRegistryManager {
        <E> Optional<MutableRegistry<E>> getOptionalMutable(RegistryKey<? extends Registry<? extends E>> var1);

        default <E> MutableRegistry<E> getMutable(RegistryKey<? extends Registry<? extends E>> key) {
            return this.getOptionalMutable(key).orElseThrow(() -> new IllegalStateException("Missing registry: " + key));
        }
    }

    final class ImmutableImpl
    implements Immutable {
        private final Map<? extends RegistryKey<? extends Registry<?>>, ? extends Registry<?>> registries;

        public ImmutableImpl(Map<? extends RegistryKey<? extends Registry<?>>, ? extends Registry<?>> registries) {
            this.registries = Map.copyOf(registries);
        }

        ImmutableImpl(Stream<Entry<?>> stream) {
            this.registries = stream.collect(ImmutableMap.toImmutableMap(Entry::key, Entry::value));
        }

        @Override
        public <E> Optional<Registry<E>> getOptionalManaged(RegistryKey<? extends Registry<? extends E>> key) {
            //noinspection unchecked
            return Optional.ofNullable(this.registries.get(key)).map(registry -> (Registry<E>) registry);
        }

        @Override
        public Stream<Entry<?>> streamManagedRegistries() {
            return this.registries.entrySet().stream().map(Entry::of);
        }
    }

    record Entry<T>(RegistryKey<? extends Registry<T>> key, Registry<T> value) {
        private static <T, R extends Registry<? extends T>> Entry<T> of(Map.Entry<? extends RegistryKey<? extends Registry<?>>, R> entry) {
            return Entry.of(entry.getKey(), entry.getValue());
        }

        private static <T> Entry<T> of(RegistryEntry.Reference<? extends Registry<? extends T>> entry) {
            return Entry.of(entry.registryKey(), entry.value());
        }

        private static <T> Entry<T> of(RegistryKey<? extends Registry<?>> key, Registry<?> value) {
            //noinspection unchecked
            return new Entry<>((RegistryKey<? extends Registry<T>>) key, (Registry<T>) value);
        }

        private Entry<T> freeze() {
            return new Entry<T>(this.key, this.value.freeze());
        }
    }

    interface Immutable
    extends DynamicRegistryManager {
        @Override
        default Immutable toImmutable() {
            return this;
        }
    }
}

