package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.*;
import lombok.Getter;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class SimpleRegistry<T> extends MutableRegistry<T> {

    private final ReferenceList<RegistryEntry.Reference<T>> rawIdToEntry = new ReferenceArrayList<>(256);
    private final Reference2IntMap<T> entryToRawId = Util.make(new Reference2IntOpenHashMap<>(), map -> map.defaultReturnValue(-1));
    private final Reference2ReferenceMap<Identifier, RegistryEntry.Reference<T>> idToEntry = new Reference2ReferenceOpenHashMap<>();
    private final Reference2ReferenceMap<RegistryKey<T>, RegistryEntry.Reference<T>> keyToEntry = new Reference2ReferenceOpenHashMap<>();
    private final Reference2ReferenceMap<T, RegistryEntry.Reference<T>> valueToEntry = new Reference2ReferenceOpenHashMap<>();
    private final Reference2ReferenceMap<T, Lifecycle> entryToLifecycle = new Reference2ReferenceOpenHashMap<>();
    private Lifecycle lifecycle;
    private volatile Reference2ReferenceMap<TagKey<T>, RegistryEntryList.Named<T>> tagToEntryList = new Reference2ReferenceOpenHashMap<>();
    private boolean frozen;
    @Nullable
    private final Function<T, RegistryEntry.Reference<T>> valueToEntryFunction;
    @Nullable
    private Reference2ReferenceMap<T, RegistryEntry.Reference<T>> unfrozenValueToEntry;
    @Nullable
    private List<RegistryEntry.Reference<T>> cachedEntries;
    @Getter
    private int nextId;

    public SimpleRegistry(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, @Nullable Function<T, RegistryEntry.Reference<T>> valueToEntryFunction) {
        super(key, lifecycle);
        this.lifecycle = lifecycle;
        this.valueToEntryFunction = valueToEntryFunction;
        if (valueToEntryFunction != null) {
            this.unfrozenValueToEntry = new Reference2ReferenceOpenHashMap<>();
        }
    }

    private List<RegistryEntry.Reference<T>> getEntries() {
        if (this.cachedEntries == null) {
            this.cachedEntries = this.rawIdToEntry.stream().filter(Objects::nonNull).toList();
        }
        return this.cachedEntries;
    }

    private void assertNotFrozen(RegistryKey<T> key) {
        if (this.frozen) {
            throw new IllegalStateException("Registry is already frozen (trying to add key " + key + ")");
        }
    }

    @Override
    public RegistryEntry<T> set(int rawId, RegistryKey<T> key, T value, Lifecycle lifecycle) {
        return this.set(rawId, key, value, lifecycle, true);
    }

    private RegistryEntry<T> set(int rawId, RegistryKey<T> key2, T value, Lifecycle lifecycle, boolean checkDuplicateKeys) {
        RegistryEntry.Reference<T> reference;
        this.assertNotFrozen(key2);
        Validate.notNull(key2);
        Validate.notNull(value);
        this.rawIdToEntry.size(Math.max(this.rawIdToEntry.size(), rawId + 1));
        this.entryToRawId.put(value, rawId);
        this.cachedEntries = null;
        if (checkDuplicateKeys && this.keyToEntry.containsKey(key2)) {
            Util.error("Adding duplicate key '" + key2 + "' to registry");
        }
        if (this.valueToEntry.containsKey(value)) {
            Util.error("Adding duplicate value '" + value + "' to registry");
        }
        this.entryToLifecycle.put(value, lifecycle);
        this.lifecycle = this.lifecycle.add(lifecycle);
        if (this.nextId <= rawId) {
            this.nextId = rawId + 1;
        }
        if (this.valueToEntryFunction != null) {
            reference = this.valueToEntryFunction.apply(value);
            RegistryEntry.Reference<T> reference2 = this.keyToEntry.put(key2, reference);
            if (reference2 != null && reference2 != reference) {
                throw new IllegalStateException("Invalid holder present for key " + key2);
            }
        } else {
            reference = this.keyToEntry.computeIfAbsent(key2, (Function<? super RegistryKey<T>, ? extends RegistryEntry.Reference<T>>) key -> RegistryEntry.Reference.standAlone(this, key));
        }
        this.idToEntry.put(key2.getValue(), reference);
        this.valueToEntry.put(value, reference);
        reference.setKeyAndValue(key2, value);
        this.rawIdToEntry.set(rawId, reference);
        return reference;
    }

    @Override
    public RegistryEntry<T> add(RegistryKey<T> key, T entry, Lifecycle lifecycle) {
        return this.set(this.nextId, key, entry, lifecycle);
    }

    @Override
    public RegistryEntry<T> replace(OptionalInt rawId, RegistryKey<T> key, T newEntry, Lifecycle lifecycle) {
        int i;
        this.assertNotFrozen(key);
        Validate.notNull(key);
        Validate.notNull(newEntry);
        RegistryEntry<T> registryEntry = this.keyToEntry.get(key);
        T object = registryEntry != null && registryEntry.hasKeyAndValue() ? registryEntry.value() : null;
        if (object == null) {
            i = rawId.orElse(this.nextId);
        } else {
            i = this.entryToRawId.getInt(object);
            if (rawId.isPresent() && rawId.getAsInt() != i) {
                throw new IllegalStateException("ID mismatch");
            }
            this.entryToLifecycle.remove(object);
            this.entryToRawId.removeInt(object);
            this.valueToEntry.remove(object);
        }
        return this.set(i, key, newEntry, lifecycle, false);
    }

    @Override
    @Nullable
    public Identifier getId(T value) {
        RegistryEntry.Reference<T> reference = this.valueToEntry.get(value);
        return reference != null ? reference.registryKey().getValue() : null;
    }

    @Override
    public Optional<RegistryKey<T>> getKey(T entry) {
        return Optional.ofNullable(this.valueToEntry.get(entry)).map(RegistryEntry.Reference::registryKey);
    }

    @Override
    public int getRawId(@Nullable T value) {
        return this.entryToRawId.getInt(value);
    }

    @Override
    @Nullable
    public T get(@Nullable RegistryKey<T> key) {
        return SimpleRegistry.getValue(this.keyToEntry.get(key));
    }

    @Override
    @Nullable
    public T get(int index) {
        if (index < 0 || index >= this.rawIdToEntry.size()) {
            return null;
        }
        return SimpleRegistry.getValue(this.rawIdToEntry.get(index));
    }

    @Override
    public Optional<RegistryEntry<T>> getEntry(int rawId) {
        if (rawId < 0 || rawId >= this.rawIdToEntry.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.rawIdToEntry.get(rawId));
    }

    @Override
    public Optional<RegistryEntry<T>> getEntry(RegistryKey<T> key) {
        return Optional.ofNullable(this.keyToEntry.get(key));
    }

    @Override
    public RegistryEntry<T> getOrCreateEntry(RegistryKey<T> key2) {
        return this.keyToEntry.computeIfAbsent(key2, (Function<? super RegistryKey<T>, ? extends RegistryEntry.Reference<T>>) key -> {
            if (this.valueToEntryFunction != null) {
                throw new IllegalStateException("This registry can't create new holders without value");
            }
            this.assertNotFrozen(key);
            return RegistryEntry.Reference.standAlone(this, key);
        });
    }

    @Override
    public DataResult<RegistryEntry<T>> getOrCreateEntryDataResult(RegistryKey<T> key) {
        RegistryEntry.Reference<T> reference = this.keyToEntry.get(key);
        if (reference == null) {
            if (this.valueToEntryFunction != null) {
                return DataResult.error(() -> "This registry can't create new holders without value (requested key: " + key + ")");
            }
            if (this.frozen) {
                return DataResult.error(() -> "Registry is already frozen (requested key: " + key + ")");
            }
            reference = RegistryEntry.Reference.standAlone(this, key);
            this.keyToEntry.put(key, reference);
        }
        return DataResult.success(reference);
    }

    @Override
    public int size() {
        return this.keyToEntry.size();
    }

    @Override
    public Lifecycle getEntryLifecycle(T entry) {
        return this.entryToLifecycle.get(entry);
    }

    @Override
    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.transform(this.getEntries().iterator(), RegistryEntry::value);
    }

    @Override
    @Nullable
    public T get(@Nullable Identifier id) {
        RegistryEntry.Reference<T> reference = this.idToEntry.get(id);
        return SimpleRegistry.getValue(reference);
    }

    @Nullable
    private static <T> T getValue(@Nullable RegistryEntry.Reference<T> entry) {
        return entry != null ? entry.value() : null;
    }

    @Override
    public Set<Identifier> getIds() {
        return Collections.unmodifiableSet(this.idToEntry.keySet());
    }

    @Override
    public Set<RegistryKey<T>> getKeys() {
        return Collections.unmodifiableSet(this.keyToEntry.keySet());
    }

    @Override
    public Set<Map.Entry<RegistryKey<T>, T>> getEntrySet() {
        return Collections.unmodifiableSet(Maps.transformValues(this.keyToEntry, RegistryEntry::value).entrySet());
    }

    @Override
    public Stream<RegistryEntry.Reference<T>> streamEntries() {
        return this.getEntries().stream();
    }

    @Override
    public boolean containsTag(TagKey<T> tag) {
        return this.tagToEntryList.containsKey(tag);
    }

    @Override
    public Stream<Pair<TagKey<T>, RegistryEntryList.Named<T>>> streamTagsAndEntries() {
        return this.tagToEntryList.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public RegistryEntryList.Named<T> getOrCreateEntryList(TagKey<T> tag) {
        RegistryEntryList.Named<T> named = this.tagToEntryList.get(tag);
        if (named == null) {
            named = this.createNamedEntryList(tag);
            Reference2ReferenceMap<TagKey<T>, RegistryEntryList.Named<T>> map = new Reference2ReferenceOpenHashMap<>(this.tagToEntryList);
            map.put(tag, named);
            this.tagToEntryList = map;
        }
        return named;
    }

    private RegistryEntryList.Named<T> createNamedEntryList(TagKey<T> tag) {
        return new RegistryEntryList.Named<>(this, tag);
    }

    @Override
    public Stream<TagKey<T>> streamTags() {
        return this.tagToEntryList.keySet().stream();
    }

    @Override
    public boolean isEmpty() {
        return this.keyToEntry.isEmpty();
    }

    @Override
    public Optional<RegistryEntry<T>> getRandom(Random random) {
        return Util.getRandomOrEmpty(this.getEntries(), random).map(RegistryEntry::upcast);
    }

    @Override
    public boolean containsId(Identifier id) {
        return this.idToEntry.containsKey(id);
    }

    @Override
    public boolean contains(RegistryKey<T> key) {
        return this.keyToEntry.containsKey(key);
    }

    @Override
    public Registry<T> freeze() {
        this.frozen = true;
        List<Identifier> list = this.keyToEntry.entrySet().stream().filter(entry -> !entry.getValue().hasKeyAndValue()).map(entry -> entry.getKey().getValue()).sorted().toList();
        if (!list.isEmpty()) {
            throw new IllegalStateException("Unbound values in registry " + this.getKey() + ": " + list);
        }
        if (this.unfrozenValueToEntry != null) {
            List<RegistryEntry.Reference<T>> list2 = this.unfrozenValueToEntry.values().stream().filter(entry -> !entry.hasKeyAndValue()).toList();
            if (!list2.isEmpty()) {
                throw new IllegalStateException("Some intrusive holders were not added to registry: " + list2);
            }
            this.unfrozenValueToEntry = null;
        }
        return this;
    }

    @Override
    public RegistryEntry.Reference<T> createEntry(T value) {
        if (this.valueToEntryFunction == null) {
            throw new IllegalStateException("This registry can't create intrusive holders");
        }
        if (this.frozen || this.unfrozenValueToEntry == null) {
            throw new IllegalStateException("Registry is already frozen");
        }
        //noinspection unchecked,deprecation
        return this.unfrozenValueToEntry.computeIfAbsent(value, key -> RegistryEntry.Reference.intrusive(this, (T) key));
    }

    @Override
    public Optional<RegistryEntryList.Named<T>> getEntryList(TagKey<T> tag) {
        return Optional.ofNullable(this.tagToEntryList.get(tag));
    }

    @Override
    public void populateTags(Map<TagKey<T>, List<RegistryEntry<T>>> tagEntries) {
        Reference2ReferenceMap<RegistryEntry.Reference<T>, List<TagKey<T>>> map = new Reference2ReferenceOpenHashMap<>();
        this.keyToEntry.values().forEach(entry -> map.put(entry, new ArrayList<>()));
        tagEntries.forEach((tag, entries) -> {
            for (RegistryEntry<T> registryEntry : entries) {
                if (!registryEntry.matchesRegistry(this)) {
                    throw new IllegalStateException("Can't create named set " + tag + " containing value " + registryEntry + " from outside registry " + this);
                }
                if (registryEntry instanceof RegistryEntry.Reference<T> reference) {
                    map.get(reference).add(tag);
                    continue;
                }
                throw new IllegalStateException("Found direct holder " + registryEntry + " value in tag " + tag);
            }
        });
        Sets.SetView<TagKey<T>> set = Sets.difference(this.tagToEntryList.keySet(), tagEntries.keySet());
        if (!set.isEmpty()) {
            LOGGER.warn("Not all defined tags for registry {} are present in data pack: {}", this.getKey(), set.stream().map(tag -> tag.id().toString()).sorted().collect(Collectors.joining(", ")));
        }
        Reference2ReferenceMap<TagKey<T>, RegistryEntryList.Named<T>> map2 = new Reference2ReferenceOpenHashMap<>(this.tagToEntryList);
        tagEntries.forEach((tag, entries) -> map2.computeIfAbsent(tag, this::createNamedEntryList).copyOf(entries));
        map.forEach(RegistryEntry.Reference::setTags);
        this.tagToEntryList = map2;
    }

    @Override
    public void clearTags() {
        this.tagToEntryList.values().forEach(entryList -> entryList.copyOf(List.of()));
        this.keyToEntry.values().forEach(entry -> entry.setTags(Set.of()));
    }
}

