package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;

/**
 * A utility class for serialization of registries using codecs.
 */
public class RegistryCodecs {
    private static <T> MapCodec<RegistryManagerEntry<T>> managerEntry(RegistryKey<? extends Registry<T>> registryRef, MapCodec<T> elementCodec) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                RegistryKey.createCodec(registryRef).fieldOf("name").forGetter(RegistryManagerEntry::key),
                Codec.INT.fieldOf("id").forGetter(RegistryManagerEntry::rawId),
                elementCodec.forGetter(RegistryManagerEntry::value)
        ).apply(instance, RegistryManagerEntry::new));
    }

    public static <T> Codec<Registry<T>> createRegistryCodec(RegistryKey<? extends Registry<T>> registryRef, Lifecycle lifecycle, Codec<T> elementCodec) {
        return RegistryCodecs.managerEntry(registryRef, elementCodec.fieldOf("element")).codec().listOf().xmap(entries -> {
            SimpleRegistry<T> mutableRegistry = new SimpleRegistry<>(registryRef, lifecycle);
            for (RegistryManagerEntry<T> registryManagerEntry : entries)
                mutableRegistry.set(registryManagerEntry.rawId(), registryManagerEntry.key(), registryManagerEntry.value(), lifecycle);
            return mutableRegistry;
        }, registry -> {
            ImmutableList.Builder<RegistryManagerEntry<T>> builder = ImmutableList.builder();
            for (T object : registry)
                builder.add(new RegistryManagerEntry<>(registry.getKey(object).orElseThrow(), registry.getRawId(object), object));
            return builder.build();
        });
    }

    public static <E> Codec<Registry<E>> createKeyedRegistryCodec(RegistryKey<? extends Registry<E>> registryRef, Lifecycle lifecycle, Codec<E> elementCodec) {
        UnboundedMapCodec<RegistryKey<E>, E> codec = Codec.unboundedMap(RegistryKey.createCodec(registryRef), elementCodec);
        return codec.xmap(entries -> {
            SimpleRegistry<E> mutableRegistry = new SimpleRegistry<>(registryRef, lifecycle);
            entries.forEach((key, value) -> mutableRegistry.add(key, value, lifecycle));
            return mutableRegistry.freeze();
        }, registry -> ImmutableMap.copyOf(registry.getEntrySet()));
    }

    public static <E> Codec<RegistryEntryList<E>> entryList(RegistryKey<? extends Registry<E>> registryRef, Codec<E> elementCodec) {
        return RegistryCodecs.entryList(registryRef, elementCodec, false);
    }

    /**
     * @param alwaysSerializeAsList whether to always serialize the list as a list
     * instead of serializing as one entry if the length is {@code 0}
     */
    public static <E> Codec<RegistryEntryList<E>> entryList(RegistryKey<? extends Registry<E>> registryRef, Codec<E> elementCodec, boolean alwaysSerializeAsList) {
        return RegistryEntryListCodec.create(registryRef, RegistryElementCodec.of(registryRef, elementCodec), alwaysSerializeAsList);
    }

    public static <E> Codec<RegistryEntryList<E>> entryList(RegistryKey<? extends Registry<E>> registryRef) {
        return RegistryCodecs.entryList(registryRef, false);
    }

    /**
     * @param alwaysSerializeAsList whether to always serialize the list as a list
     * instead of serializing as one entry if the length is {@code 0}
     */
    public static <E> Codec<RegistryEntryList<E>> entryList(RegistryKey<? extends Registry<E>> registryRef, boolean alwaysSerializeAsList) {
        return RegistryEntryListCodec.create(registryRef, RegistryFixedCodec.of(registryRef), alwaysSerializeAsList);
    }

    record RegistryManagerEntry<T>(RegistryKey<T> key, int rawId, T value) {}
}

