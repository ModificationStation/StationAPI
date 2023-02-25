package net.modificationstation.stationapi.api.util.dynamic;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.registry.*;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class RegistryLoader {
    private final EntryLoader entryLoader;
    private final Map<RegistryKey<? extends Registry<?>>, ValueHolder<?>> valueHolders = new IdentityHashMap<>();

    RegistryLoader(EntryLoader entryLoader) {
        this.entryLoader = entryLoader;
    }

    public <E> DataResult<? extends Registry<E>> load(MutableRegistry<E> registry, RegistryKey<? extends Registry<E>> registryRef, Codec<E> codec, DynamicOps<JsonElement> ops) {
        Map<RegistryKey<E>, EntryLoader.Parseable<E>> map = this.entryLoader.getKnownEntryPaths(registryRef);
        DataResult<MutableRegistry<E>> dataResult = DataResult.success(registry, Lifecycle.stable());
        for (Map.Entry<RegistryKey<E>, EntryLoader.Parseable<E>> entry : map.entrySet()) {
            dataResult = dataResult.flatMap(reg -> this.load(reg, registryRef, codec, entry.getKey(), Optional.of(entry.getValue()), ops).map(entry1 -> reg));
        }
        return dataResult.setPartial(registry);
    }

    <E> DataResult<RegistryEntry<E>> load(MutableRegistry<E> registry, RegistryKey<? extends Registry<E>> registryRef, Codec<E> codec, RegistryKey<E> entryKey, DynamicOps<JsonElement> ops) {
        Optional<EntryLoader.Parseable<E>> optional = this.entryLoader.createParseable(entryKey);
        return this.load(registry, registryRef, codec, entryKey, optional, ops);
    }

    private <E> DataResult<RegistryEntry<E>> load(MutableRegistry<E> registry, RegistryKey<? extends Registry<E>> registryRef, Codec<E> codec, RegistryKey<E> entryKey, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<EntryLoader.Parseable<E>> parseable, DynamicOps<JsonElement> ops) {
        DataResult<RegistryEntry<E>> dataResult2;
        ValueHolder<E> valueHolder = this.getOrCreateValueHolder(registryRef);
        DataResult<RegistryEntry<E>> dataResult = valueHolder.values.get(entryKey);
        if (dataResult != null) {
            return dataResult;
        }
        RegistryEntry<E> registryEntry = registry.getOrCreateEntry(entryKey);
        valueHolder.values.put(entryKey, DataResult.success(registryEntry));
        if (parseable.isEmpty()) {
            dataResult2 = registry.contains(entryKey) ? DataResult.success(registryEntry, Lifecycle.stable()) : DataResult.error(() -> "Missing referenced custom/removed registry entry for registry " + registryRef + " named " + entryKey.getValue());
        } else {
            DataResult<EntryLoader.Entry<E>> dataResult3 = parseable.get().parseElement(ops, codec);
            Optional<EntryLoader.Entry<E>> optional = dataResult3.result();
            if (optional.isPresent()) {
                EntryLoader.Entry<E> entry2 = optional.get();
                registry.replace(entry2.fixedId(), entryKey, entry2.value(), dataResult3.lifecycle());
            }
            dataResult2 = dataResult3.map(entry -> registryEntry);
        }
        valueHolder.values.put(entryKey, dataResult2);
        return dataResult2;
    }

    private <E> ValueHolder<E> getOrCreateValueHolder(RegistryKey<? extends Registry<E>> registryRef) {
        //noinspection unchecked
        return (ValueHolder<E>) this.valueHolders.computeIfAbsent(registryRef, ref -> new ValueHolder<>());
    }

    public LoaderAccess createAccess(DynamicRegistryManager.Mutable dynamicRegistryManager) {
        return new LoaderAccess(dynamicRegistryManager, this);
    }

    static final class ValueHolder<E> {
        final Map<RegistryKey<E>, DataResult<RegistryEntry<E>>> values = Maps.newIdentityHashMap();

        ValueHolder() {
        }
    }

    public record LoaderAccess(DynamicRegistryManager.Mutable dynamicRegistryManager, RegistryLoader loader) {
        public <E> DataResult<? extends Registry<E>> load(RegistryKey<? extends Registry<E>> registryRef, Codec<E> codec, DynamicOps<JsonElement> ops) {
            MutableRegistry<E> mutableRegistry = this.dynamicRegistryManager.getMutable(registryRef);
            return this.loader.load(mutableRegistry, registryRef, codec, ops);
        }

        public <E> DataResult<RegistryEntry<E>> load(RegistryKey<? extends Registry<E>> registryRef, Codec<E> codec, RegistryKey<E> entryKey, DynamicOps<JsonElement> ops) {
            MutableRegistry<E> mutableRegistry = this.dynamicRegistryManager.getMutable(registryRef);
            return this.loader.load(mutableRegistry, registryRef, codec, entryKey, ops);
        }
    }
}

