package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.*;
import lombok.Getter;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedCallback;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryRemovedCallback;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapCallback;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.registry.sync.RemapStateImpl;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class SimpleRegistry<T> implements MutableRegistry<T>, RemappableRegistry, ListenableRegistry<T> {
    final RegistryKey<? extends Registry<T>> key;
    private final ObjectList<RegistryEntry.Reference<T>> rawIdToEntry;
    private final Object2IntMap<T> entryToRawId;
    private final Map<Identifier, RegistryEntry.Reference<T>> idToEntry;
    private final Map<RegistryKey<T>, RegistryEntry.Reference<T>> keyToEntry;
    private final Map<T, RegistryEntry.Reference<T>> valueToEntry;
    private final Map<T, Lifecycle> entryToLifecycle;
    private Lifecycle lifecycle;
    private volatile Map<TagKey<T>, RegistryEntryList.Named<T>> tagToEntryList;
    private boolean frozen;
    @Nullable
    private Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry;
    @Nullable
    private List<RegistryEntry.Reference<T>> cachedEntries;
    @Getter
    private int nextId;
    private final RegistryWrapper.Impl<T> wrapper;
    private Reference2IntMap<Identifier> prevIndexedEntries;
    private BiMap<Identifier, RegistryEntry.Reference<T>> prevEntries;

    @Getter
    private final Event<RegistryEntryAddedCallback<T>> addObjectEvent = EventFactory.createWithPhases(RegistryEntryAddedCallback.class,
            callbacks -> (rawId, id, object) -> {
                for (RegistryEntryAddedCallback<T> callback : callbacks) callback.onEntryAdded(rawId, id, object);
            },
            StationAPI.INTERNAL_PHASE, Event.DEFAULT_PHASE
    );

    @Getter
    private final Event<RegistryEntryRemovedCallback<T>> removeObjectEvent = EventFactory.createWithPhases(RegistryEntryRemovedCallback.class,
            callbacks -> (rawId, id, object) -> {
                for (RegistryEntryRemovedCallback<T> callback : callbacks) callback.onEntryRemoved(rawId, id, object);
            },
            StationAPI.INTERNAL_PHASE, Event.DEFAULT_PHASE
    );

    @Getter
    private final Event<RegistryIdRemapCallback<T>> remapEvent = EventFactory.createWithPhases(RegistryIdRemapCallback.class,
            callbacks -> a -> {
                for (RegistryIdRemapCallback<T> callback : callbacks) callback.onRemap(a);
            },
            StationAPI.INTERNAL_PHASE, Event.DEFAULT_PHASE
    );

    public SimpleRegistry(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        this(key, lifecycle, false);
    }

    public SimpleRegistry(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, boolean intrusive) {
        this.rawIdToEntry = new ObjectArrayList<>(256);
        this.entryToRawId = Util.make(new Object2IntOpenCustomHashMap<>(Util.identityHashStrategy()), map -> map.defaultReturnValue(-1));
        this.idToEntry = new HashMap<>();
        this.keyToEntry = new HashMap<>();
        this.valueToEntry = new IdentityHashMap<>();
        this.entryToLifecycle = new IdentityHashMap<>();
        this.tagToEntryList = new IdentityHashMap<>();
        this.wrapper = new RegistryWrapper.Impl<>() {
            @Override
            public RegistryKey<? extends Registry<? extends T>> getRegistryKey() {
                return SimpleRegistry.this.key;
            }

            @Override
            public Lifecycle getLifecycle() {
                return SimpleRegistry.this.getLifecycle();
            }

            @Override
            public Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> key) {
                return SimpleRegistry.this.getEntry(key);
            }

            @Override
            public Stream<RegistryEntry.Reference<T>> streamEntries() {
                return SimpleRegistry.this.streamEntries();
            }

            @Override
            public Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> tag) {
                return SimpleRegistry.this.getEntryList(tag);
            }

            @Override
            public Stream<RegistryEntryList.Named<T>> streamTags() {
                return SimpleRegistry.this.streamTagsAndEntries().map(Pair::getSecond);
            }
        };
        this.key = key;
        this.lifecycle = lifecycle;
        if (intrusive) this.intrusiveValueToEntry = new IdentityHashMap<>();
    }

    @Override
    public RegistryKey<? extends Registry<T>> getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return "Registry[" + this.key + " (" + this.lifecycle + ")]";
    }

    private List<RegistryEntry.Reference<T>> getEntries() {
        if (this.cachedEntries == null)
            this.cachedEntries = this.rawIdToEntry.stream().filter(Objects::nonNull).toList();

        return this.cachedEntries;
    }

    private void assertNotFrozen() {
        if (this.frozen) throw new IllegalStateException("Registry is already frozen");
    }

    private void assertNotFrozen(RegistryKey<T> key) {
        if (this.frozen) throw new IllegalStateException("Registry is already frozen (trying to add key " + key + ")");
    }

    @Override
    public RegistryEntry.Reference<T> set(int rawId, RegistryKey<T> registryKey, T value, Lifecycle lifecycle) {
        int indexedEntriesId = entryToRawId.getInt(value);
        if (indexedEntriesId >= 0)
            throw new RuntimeException("Attempted to register object " + value + " twice! (at raw IDs " + indexedEntriesId + " and " + rawId + " )");
        boolean isObjectNew;
        if (!idToEntry.containsKey(registryKey.getValue())) isObjectNew = true;
        else {
            RegistryEntry.Reference<T> oldObject = idToEntry.get(registryKey.getValue());
            if (oldObject != null && oldObject.value() != null && oldObject.value() != value) {
                int oldId = entryToRawId.getInt(oldObject.value());
                if (oldId != rawId)
                    throw new RuntimeException("Attempted to register ID " + registryKey + " at different raw IDs (" + oldId + ", " + rawId + ")! If you're trying to override an item, use .set(), not .register()!");
                removeObjectEvent.invoker().onEntryRemoved(oldId, registryKey.getValue(), oldObject.value());
                isObjectNew = true;
            } else isObjectNew = false;
        }
        this.assertNotFrozen(registryKey);
        Validate.notNull(registryKey);
        Validate.notNull(value);
        if (this.idToEntry.containsKey(registryKey.getValue()))
            Util.throwOrPause(new IllegalStateException("Adding duplicate key '" + registryKey + "' to registry"));

        if (this.valueToEntry.containsKey(value))
            Util.throwOrPause(new IllegalStateException("Adding duplicate value '" + value + "' to registry"));

        RegistryEntry.Reference<T> reference;
        if (this.intrusiveValueToEntry != null) {
            reference = this.intrusiveValueToEntry.remove(value);
            if (reference == null)
                throw new AssertionError("Missing intrusive holder for " + registryKey + ":" + value);

            reference.setRegistryKey(registryKey);
        } else reference = this.keyToEntry.computeIfAbsent(registryKey, key -> RegistryEntry.Reference.standAlone(this.getEntryOwner(), key));

        this.keyToEntry.put(registryKey, reference);
        this.idToEntry.put(registryKey.getValue(), reference);
        this.valueToEntry.put(value, reference);
        this.rawIdToEntry.size(Math.max(this.rawIdToEntry.size(), rawId + 1));
        this.rawIdToEntry.set(rawId, reference);
        this.entryToRawId.put(value, rawId);
        if (this.nextId <= rawId) this.nextId = rawId + 1;

        this.entryToLifecycle.put(value, lifecycle);
        this.lifecycle = this.lifecycle.add(lifecycle);
        this.cachedEntries = null;

        // shouldn't be here really
        // but it's easier when you can interact with unfrozen registries
        reference.setValue(value);

        if (isObjectNew)
            addObjectEvent.invoker().onEntryAdded(rawId, registryKey.getValue(), value);

        return reference;
    }

    @Override
    public RegistryEntry.Reference<T> add(RegistryKey<T> registryKey, T value, Lifecycle lifecycle) {
        return this.set(this.nextId, registryKey, value, lifecycle);
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
        return getValue(this.keyToEntry.get(key));
    }

    @Override
    @Nullable
    public T get(int index) {
        return index >= 0 && index < this.rawIdToEntry.size() ? getValue(this.rawIdToEntry.get(index)) : null;
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getEntry(int rawId) {
        return rawId >= 0 && rawId < this.rawIdToEntry.size() ? Optional.ofNullable(this.rawIdToEntry.get(rawId)) : Optional.empty();
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getEntry(RegistryKey<T> key) {
        return Optional.ofNullable(this.keyToEntry.get(key));
    }

    @Override
    public RegistryEntry<T> getEntry(T value) {
        RegistryEntry.Reference<T> reference = this.valueToEntry.get(value);
        return reference != null ? reference : RegistryEntry.of(value);
    }

    RegistryEntry.Reference<T> getOrCreateEntry(RegistryKey<T> key) {
        return this.keyToEntry.computeIfAbsent(key, key2 -> {
            if (this.intrusiveValueToEntry != null)
                throw new IllegalStateException("This registry can't create new holders without value");
            else {
                this.assertNotFrozen(key2);
                return RegistryEntry.Reference.standAlone(this.getEntryOwner(), key2);
            }
        });
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
        return getValue(reference);
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
    public Stream<Pair<TagKey<T>, RegistryEntryList.Named<T>>> streamTagsAndEntries() {
        return this.tagToEntryList.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public RegistryEntryList.Named<T> getOrCreateEntryList(TagKey<T> tag) {
        RegistryEntryList.Named<T> named = this.tagToEntryList.get(tag);
        if (named == null) {
            named = this.createNamedEntryList(tag);
            Map<TagKey<T>, RegistryEntryList.Named<T>> map = new IdentityHashMap<>(this.tagToEntryList);
            map.put(tag, named);
            this.tagToEntryList = map;
        }

        return named;
    }

    private RegistryEntryList.Named<T> createNamedEntryList(TagKey<T> tag) {
        return new RegistryEntryList.Named<>(getEntryOwner(), tag);
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
    public Optional<RegistryEntry.Reference<T>> getRandom(Random random) {
        return Util.getRandomOrEmpty(this.getEntries(), random);
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
        if (this.frozen) return this;
        else {
            this.frozen = true;
            this.valueToEntry.forEach((value, entry) -> entry.setValue(value));
            List<Identifier> list = this.keyToEntry.entrySet().stream().filter(entry -> !entry.getValue().hasKeyAndValue()).map(entry -> entry.getKey().getValue()).sorted().toList();
            if (!list.isEmpty()) {
                throw new IllegalStateException("Unbound values in registry " + this.getKey() + ": " + list);
            } else {
                if (this.intrusiveValueToEntry != null) {
                    if (!this.intrusiveValueToEntry.isEmpty())
                        throw new IllegalStateException("Some intrusive holders were not registered: " + this.intrusiveValueToEntry.values());

                    this.intrusiveValueToEntry = null;
                }

                return this;
            }
        }
    }

    @Override
    public RegistryEntry.Reference<T> createEntry(T value) {
        if (intrusiveValueToEntry == null)
            throw new IllegalStateException("This registry can't create intrusive holders");
        else {
            this.assertNotFrozen();
            return intrusiveValueToEntry.computeIfAbsent(value, valuex -> RegistryEntry.Reference.intrusive(this.getReadOnlyWrapper(), valuex));
        }
    }

    @Override
    public Optional<RegistryEntryList.Named<T>> getEntryList(TagKey<T> tag) {
        return Optional.ofNullable(this.tagToEntryList.get(tag));
    }

    @Override
    public void populateTags(Map<TagKey<T>, List<RegistryEntry<T>>> tagEntries) {
        Map<RegistryEntry.Reference<T>, List<TagKey<T>>> map = new IdentityHashMap<>();
        this.keyToEntry.values().forEach(entry -> map.put(entry, new ArrayList<>()));
        tagEntries.forEach((tag, entries) -> {

            for (RegistryEntry<T> entry : entries) {
                if (!entry.ownerEquals(this.getReadOnlyWrapper()))
                    throw new IllegalStateException("Can't create named set " + tag + " containing value " + entry + " from outside registry " + this);

                if (!(entry instanceof RegistryEntry.Reference<T> reference))
                    throw new IllegalStateException("Found direct holder " + entry + " value in tag " + tag);

                map.get(reference).add(tag);
            }

        });
        Set<TagKey<T>> set = Sets.difference(this.tagToEntryList.keySet(), tagEntries.keySet());
        if (!set.isEmpty())
            LOGGER.warn("Not all defined tags for registry {} are present in data pack: {}", this.getKey(), set.stream().map(tag -> tag.id().toString()).sorted().collect(Collectors.joining(", ")));

        Map<TagKey<T>, RegistryEntryList.Named<T>> map2 = new IdentityHashMap<>(this.tagToEntryList);
        tagEntries.forEach((tag, entries) -> map2.computeIfAbsent(tag, this::createNamedEntryList).copyOf(entries));
        map.forEach(RegistryEntry.Reference::setTags);
        this.tagToEntryList = map2;
    }

    @Override
    public void clearTags() {
        this.tagToEntryList.values().forEach(entryList -> entryList.copyOf(List.of()));
        this.keyToEntry.values().forEach(entry -> entry.setTags(Set.of()));
    }

    @Override
    public RegistryEntryLookup<T> createMutableEntryLookup() {
        this.assertNotFrozen();
        return new RegistryEntryLookup<>() {
            @Override
            public Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> key) {
                return Optional.of(this.getOrThrow(key));
            }

            @Override
            public RegistryEntry.Reference<T> getOrThrow(RegistryKey<T> key) {
                return SimpleRegistry.this.getOrCreateEntry(key);
            }

            @Override
            public Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> tag) {
                return Optional.of(this.getOrThrow(tag));
            }

            @Override
            public RegistryEntryList.Named<T> getOrThrow(TagKey<T> tag) {
                return SimpleRegistry.this.getOrCreateEntryList(tag);
            }
        };
    }

    @Override
    public RegistryEntryOwner<T> getEntryOwner() {
        return this.wrapper;
    }

    @Override
    public RegistryWrapper.Impl<T> getReadOnlyWrapper() {
        return this.wrapper;
    }

    public void remap(String name, Reference2IntMap<Identifier> remoteIndexedEntries, RemapMode mode) throws RemapException {
        // Throw on invalid conditions.
        switch (mode) {
            case AUTHORITATIVE:
                break;
            case REMOTE: {
                List<String> strings = null;
                for (Identifier remoteId : remoteIndexedEntries.keySet())
                    if (!idToEntry.containsKey(remoteId)) {
                        if (strings == null) strings = new ArrayList<>();
                        strings.add(" - " + remoteId);
                    }
                if (strings != null) {
                    StringBuilder builder = new StringBuilder("Received ID map for " + name + " contains IDs unknown to the receiver!");
                    for (String s : strings) builder.append('\n').append(s);
                    throw new RemapException(builder.toString());
                }
                break;
            }
            case EXACT: {
                if (!idToEntry.keySet().equals(remoteIndexedEntries.keySet())) {
                    List<String> strings = new ArrayList<>();
                    for (Identifier remoteId : remoteIndexedEntries.keySet())
                        if (!idToEntry.containsKey(remoteId)) strings.add(" - " + remoteId + " (missing on local)");
                    for (Identifier localId : getIds())
                        if (!remoteIndexedEntries.containsKey(localId))
                            strings.add(" - " + localId + " (missing on remote)");
                    StringBuilder builder = new StringBuilder("Local and remote ID sets for " + name + " do not match!");
                    for (String s : strings) builder.append('\n').append(s);
                    throw new RemapException(builder.toString());
                }
                break;
            }
        }

        // Make a copy of the previous maps.
        // For now, only one is necessary - on an integrated server scenario,
        // AUTHORITATIVE == CLIENT, which is fine.
        // The reason we preserve the first one is because it contains the
        // vanilla order of IDs before mods, which is crucial for vanilla server
        // compatibility.
        if (prevIndexedEntries == null) {
            prevIndexedEntries = new Reference2IntOpenHashMap<>();
            prevEntries = HashBiMap.create(idToEntry);
            for (T o : this) prevIndexedEntries.put(getId(o), getRawId(o));
        }
        Int2ReferenceMap<Identifier> oldIdMap = new Int2ReferenceOpenHashMap<>();
        for (T o : this) oldIdMap.put(getRawId(o), getId(o));

        // If we're AUTHORITATIVE, we append entries which only exist on the
        // local side to the new entry list. For REMOTE, we instead drop them.
        switch (mode) {
            case AUTHORITATIVE -> {
                int maxValue = 0;
                Reference2IntMap<Identifier> oldRemoteIndexedEntries = remoteIndexedEntries;
                remoteIndexedEntries = new Reference2IntOpenHashMap<>();
                for (Identifier id : oldRemoteIndexedEntries.keySet()) {
                    int v = oldRemoteIndexedEntries.getInt(id);
                    remoteIndexedEntries.put(id, v);
                    if (v > maxValue) maxValue = v;
                }
                for (Identifier id : getIds())
                    if (!remoteIndexedEntries.containsKey(id)) {
                        LOGGER.warn("Adding " + id + " to saved/remote registry.");
                        remoteIndexedEntries.put(id, ++maxValue);
                    }
            }
            case REMOTE -> {
                int maxId = -1;
                for (Identifier id : getIds())
                    if (!remoteIndexedEntries.containsKey(id)) {
                        if (maxId < 0) for (int value : remoteIndexedEntries.values()) if (value > maxId) maxId = value;
                        if (maxId < 0)
                            throw new RemapException("Failed to assign new id to client only registry entry");
                        maxId++;
                        LOGGER.debug("An ID for {} was not sent by the server, assuming client only registry entry and assigning a new id ({}) in {}", id.toString(), maxId, getKey().getValue().toString());
                        remoteIndexedEntries.put(id, maxId);
                    }
            }
        }
        Int2IntMap idMap = new Int2IntOpenHashMap();
        for (int i = 0; i < rawIdToEntry.size(); i++) {
            RegistryEntry.Reference<T> reference = rawIdToEntry.get(i);

            // Unused id, skip
            if (reference == null) continue;
            Identifier id = reference.registryKey().getValue();

            // see above note
            if (remoteIndexedEntries.containsKey(id)) idMap.put(i, remoteIndexedEntries.getInt(id));
        }

        // entries was handled above, if it was necessary.
        rawIdToEntry.clear();
        entryToRawId.clear();
        nextId = 0;
        List<Identifier> orderedRemoteEntries = new ArrayList<>(remoteIndexedEntries.keySet());
        orderedRemoteEntries.sort(Comparator.comparingInt(remoteIndexedEntries::getInt));
        for (Identifier identifier : orderedRemoteEntries) {
            int id = remoteIndexedEntries.getInt(identifier);
            RegistryEntry.Reference<T> object = idToEntry.get(identifier);

            // Warn if an object is missing from the local registry.
            // This should only happen in AUTHORITATIVE mode, and as such we
            // throw an exception otherwise.
            if (object == null) {
                if (mode != RemapMode.AUTHORITATIVE)
                    throw new RemapException(identifier + " missing from registry, but requested!");
                else LOGGER.warn(identifier + " missing from registry, but requested!");
                continue;
            }

            // Add the new object, increment nextId to match.
            rawIdToEntry.size(Math.max(this.rawIdToEntry.size(), id + 1));
            rawIdToEntry.set(id, object);
            entryToRawId.put(object.value(), id);
            if (nextId <= id) nextId = id + 1;
        }
        remapEvent.invoker().onRemap(new RemapStateImpl<>(this, oldIdMap, idMap));
    }

    @Override
    public void unmap(String name) throws RemapException {
        if (prevIndexedEntries != null) {
            List<Identifier> addedIds = new ArrayList<>();

            // Emit AddObject events for previously culled objects.
            for (Identifier id : prevEntries.keySet())
                if (!idToEntry.containsKey(id)) {
                    assert prevIndexedEntries.containsKey(id);
                    addedIds.add(id);
                }
            idToEntry.clear();
            keyToEntry.clear();
            idToEntry.putAll(prevEntries);
            for (Map.Entry<Identifier, RegistryEntry.Reference<T>> entry : prevEntries.entrySet()) {
                RegistryKey<T> entryKey = RegistryKey.of(getKey(), entry.getKey());
                keyToEntry.put(entryKey, entry.getValue());
            }
            remap(name, prevIndexedEntries, RemapMode.AUTHORITATIVE);
            for (Identifier id : addedIds)
                addObjectEvent.invoker().onEntryAdded(entryToRawId.getInt(idToEntry.get(id)), id, get(id));
            prevIndexedEntries = null;
            prevEntries = null;
        }
    }
}
