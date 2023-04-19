/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.collection.IndexedIterable;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

public abstract class Registry<T>
implements Keyable,
        IndexedIterable<T> {
    public static final Identifier ROOT_KEY = MODID.id("root");
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    protected static final MutableRegistry<MutableRegistry<?>> ROOT = new SimpleRegistry<>(RegistryKey.ofRegistry(MODID.id("root")), Lifecycle.experimental(), null);
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public static final Registry<? extends Registry<?>> REGISTRIES = ROOT;
    /**
     * The key representing the type of elements held by this registry. It is also the
     * key of this registry within the root registry.
     */
    private final RegistryKey<? extends Registry<T>> registryKey;
    private final Lifecycle lifecycle;

    public static <T extends Registry<?>> void validate(Registry<T> registries) {
        registries.forEach((registry) -> {
            if (registry.getIds().isEmpty()) {
                Identifier var10000 = registries.getId(registry);
                Util.error("Registry '" + var10000 + "' was empty after loading");
            }

            if (registry instanceof DefaultedRegistry<?> defaultedRegistry) {
                Identifier identifier = defaultedRegistry.getDefaultId();
                Validate.notNull(registry.get(identifier), "Missing default of DefaultedMappedRegistry: " + identifier);
            }

        });
    }

    public static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, DefaultEntryGetter<T> defaultEntryGetter) {
        return Registry.create(key, Lifecycle.experimental(), defaultEntryGetter);
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, String defaultId, DefaultEntryGetter<T> defaultEntryGetter) {
        return Registry.create(key, defaultId, Lifecycle.experimental(), defaultEntryGetter);
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, String defaultId, Function<T, RegistryEntry.Reference<T>> valueToEntryFunction, DefaultEntryGetter<T> defaultEntryGetter) {
        return Registry.create(key, defaultId, Lifecycle.experimental(), valueToEntryFunction, defaultEntryGetter);
    }

    public static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, DefaultEntryGetter<T> defaultEntryGetter) {
        return Registry.create(key, new SimpleRegistry<>(key, lifecycle, null), defaultEntryGetter, lifecycle);
    }

    public static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, Function<T, RegistryEntry.Reference<T>> valueToEntryFunction, DefaultEntryGetter<T> defaultEntryGetter) {
        return Registry.create(key, new SimpleRegistry<>(key, lifecycle, valueToEntryFunction), defaultEntryGetter, lifecycle);
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, String defaultId, Lifecycle lifecycle, DefaultEntryGetter<T> defaultEntryGetter) {
        return Registry.create(key, new DefaultedRegistry<>(defaultId, key, lifecycle, null), defaultEntryGetter, lifecycle);
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, String defaultId, Lifecycle lifecycle, Function<T, RegistryEntry.Reference<T>> valueToEntryFunction, DefaultEntryGetter<T> defaultEntryGetter) {
        return Registry.create(key, new DefaultedRegistry<>(defaultId, key, lifecycle, valueToEntryFunction), defaultEntryGetter, lifecycle);
    }

    private static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, DefaultEntryGetter<T> defaultEntryGetter, Lifecycle lifecycle) {
        Identifier identifier = key.getValue();
        if (defaultEntryGetter.run(registry) == null) {
            LOGGER.error("Unable to bootstrap registry '{}'", identifier);
        }
        return Registry.create(key, registry, lifecycle);
    }

    public static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, Lifecycle lifecycle) {
        //noinspection unchecked
        ((MutableRegistry<R>) ROOT).add((RegistryKey<R>) key, registry, lifecycle);
        return registry;
    }

    protected Registry(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        this.registryKey = key;
        this.lifecycle = lifecycle;
    }

    public static void freezeRegistries() {
        for (Registry<?> registry : REGISTRIES) {
            registry.freeze();
        }
    }

    public RegistryKey<? extends Registry<T>> getKey() {
        return this.registryKey;
    }

    public Lifecycle method_39198() {
        return this.lifecycle;
    }

    public String toString() {
        return "Registry[" + this.registryKey + " (" + this.lifecycle + ")]";
    }

    public Codec<T> getCodec() {
        Codec<T> codec = Identifier.CODEC.flatXmap(id -> Optional.ofNullable(this.get(id)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + this.registryKey + ": " + id)), value -> this.getKey(value).map(RegistryKey::getValue).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + this.registryKey + ":" + value)));
        Codec<T> codec2 = Codecs.rawIdChecked(value -> this.getKey(value).isPresent() ? this.getRawId(value) : -1, this::get, -1);
        return Codecs.withLifecycle(Codecs.orCompressed(codec, codec2), this::getEntryLifecycle, value -> this.lifecycle);
    }

    public Codec<RegistryEntry<T>> createEntryCodec() {
        Codec<RegistryEntry<T>> codec = Identifier.CODEC.flatXmap(id -> this.getEntry(RegistryKey.of(this.registryKey, id)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + this.registryKey + ": " + id)), entry -> entry.getKey().map(RegistryKey::getValue).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + this.registryKey + ":" + entry)));
        return Codecs.withLifecycle(codec, entry -> this.getEntryLifecycle(entry.value()), entry -> this.lifecycle);
    }

    public <U> Stream<U> keys(DynamicOps<U> ops) {
        return this.getIds().stream().map(id -> ops.createString(id.toString()));
    }

    @Nullable
    public abstract Identifier getId(T var1);

    public Optional<Identifier> getId(int rawId) {
        return getEntry(rawId).flatMap(RegistryEntry::getKey).map(RegistryKey::getValue);
    }

    public abstract Optional<RegistryKey<T>> getKey(T var1);

    @Override
    public abstract int getRawId(@Nullable T var1);

    @Nullable
    public abstract T get(@Nullable RegistryKey<T> var1);

    @Nullable
    public abstract T get(@Nullable Identifier var1);

    /**
     * Gets the lifecycle of a registry entry.
     */
    public abstract Lifecycle getEntryLifecycle(T var1);

    public abstract Lifecycle getLifecycle();

    public Optional<T> getOrEmpty(@Nullable Identifier id) {
        return Optional.ofNullable(this.get(id));
    }

    public Optional<T> getOrEmpty(@Nullable RegistryKey<T> key) {
        return Optional.ofNullable(this.get(key));
    }

    /**
     * Gets an entry from the registry.
     * 
     * @throws IllegalStateException if the entry was not present in the registry
     */
    public T getOrThrow(RegistryKey<T> key) {
        T object = this.get(key);
        if (object == null) {
            throw new IllegalStateException("Missing key in " + this.registryKey + ": " + key);
        }
        return object;
    }

    public abstract Set<Identifier> getIds();

    public abstract Set<Map.Entry<RegistryKey<T>, T>> getEntrySet();

    public abstract Set<RegistryKey<T>> getKeys();

    public abstract Optional<RegistryEntry<T>> getRandom(Random var1);

    public Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    public abstract boolean containsId(Identifier var1);

    public abstract boolean contains(RegistryKey<T> var1);

    public static <V, T extends V> T register(Registry<V> registry, Int2ReferenceFunction<T> initializer, Identifier id) {
        return Registry.register(registry, id, initializer.apply(((MutableRegistry<V>) registry).getNextId()));
    }

    public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry) {
        return Registry.register(registry, RegistryKey.of(registry.registryKey, id), entry);
    }

    public static <V, T extends V> T register(Registry<V> registry, RegistryKey<V> key, T entry) {
        ((MutableRegistry<V>) registry).add(key, entry, Lifecycle.stable());
        return entry;
    }

    public static <V, T extends V> T register(Registry<V> registry, int rawId, Identifier id, T entry) {
        ((MutableRegistry<V>) registry).set(rawId, RegistryKey.of(registry.registryKey, id), entry, Lifecycle.stable());
        return entry;
    }

    public abstract Registry<T> freeze();

    public abstract RegistryEntry<T> getOrCreateEntry(RegistryKey<T> var1);

    public abstract DataResult<RegistryEntry<T>> getOrCreateEntryDataResult(RegistryKey<T> var1);

    public abstract RegistryEntry.Reference<T> createEntry(T var1);

    public abstract Optional<RegistryEntry<T>> getEntry(int var1);

    public abstract Optional<RegistryEntry<T>> getEntry(RegistryKey<T> var1);

    public RegistryEntry<T> entryOf(RegistryKey<T> key) {
        return this.getEntry(key).orElseThrow(() -> new IllegalStateException("Missing key in " + this.registryKey + ": " + key));
    }

    public abstract Stream<RegistryEntry.Reference<T>> streamEntries();

    public abstract Optional<RegistryEntryList.Named<T>> getEntryList(TagKey<T> var1);

    public Iterable<RegistryEntry<T>> iterateEntries(TagKey<T> tag) {
        return DataFixUtils.orElse(this.getEntryList(tag), List.of());
    }

    public abstract RegistryEntryList.Named<T> getOrCreateEntryList(TagKey<T> var1);

    public abstract Stream<Pair<TagKey<T>, RegistryEntryList.Named<T>>> streamTagsAndEntries();

    public abstract Stream<TagKey<T>> streamTags();

    public abstract boolean containsTag(TagKey<T> var1);

    public abstract void clearTags();

    public abstract void populateTags(Map<TagKey<T>, List<RegistryEntry<T>>> var1);

    public IndexedIterable<RegistryEntry<T>> getIndexedEntries() {
        return new IndexedIterable<>() {

            @Override
            public int getRawId(RegistryEntry<T> registryEntry) {
                return Registry.this.getRawId(registryEntry.value());
            }

            @Override
            @Nullable
            public RegistryEntry<T> get(int i) {
                return Registry.this.getEntry(i).orElse(null);
            }

            @Override
            public int size() {
                return Registry.this.size();
            }

            @Override
            public Iterator<RegistryEntry<T>> iterator() {
                return Registry.this.streamEntries().map(entry -> (RegistryEntry<T>) entry).iterator();
            }
        };
    }

    @FunctionalInterface
    public interface DefaultEntryGetter<T> {
        T run(Registry<T> var1);
    }
}

