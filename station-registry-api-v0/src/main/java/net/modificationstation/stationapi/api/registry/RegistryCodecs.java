package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.dynamic.RegistryLoader;
import net.modificationstation.stationapi.api.util.dynamic.RegistryOps;

import java.util.Map;

public class RegistryCodecs {
    private static <T> MapCodec<RegistryManagerEntry<T>> managerEntry(RegistryKey<? extends Registry<T>> registryRef, MapCodec<T> elementCodec) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(Identifier.CODEC.xmap(RegistryKey.createKeyFactory(registryRef), RegistryKey::getValue).fieldOf("name").forGetter(RegistryManagerEntry::key), Codec.INT.fieldOf("id").forGetter(RegistryManagerEntry::rawId), elementCodec.forGetter(RegistryManagerEntry::value)).apply(instance, RegistryManagerEntry::new));
    }

    public static <T> Codec<Registry<T>> createRegistryCodec(RegistryKey<? extends Registry<T>> registryRef, Lifecycle lifecycle, Codec<T> elementCodec) {
        return RegistryCodecs.managerEntry(registryRef, elementCodec.fieldOf("element")).codec().listOf().xmap(entries -> {
            SimpleRegistry<T> mutableRegistry = new SimpleRegistry<>(registryRef, lifecycle, null);
            for (RegistryManagerEntry<T> registryManagerEntry : entries) {
                ((MutableRegistry<T>) mutableRegistry).set(registryManagerEntry.rawId(), registryManagerEntry.key(), registryManagerEntry.value(), lifecycle);
            }
            return mutableRegistry;
        }, registry -> {
            ImmutableList.Builder<RegistryManagerEntry<T>> builder = ImmutableList.builder();
            for (T object : registry) {
                builder.add(new RegistryManagerEntry<>(registry.getKey(object).orElseThrow(), registry.getRawId(object), object));
            }
            return builder.build();
        });
    }

    public static <E> Codec<Registry<E>> dynamicRegistry(RegistryKey<? extends Registry<E>> registryRef, Lifecycle lifecycle, Codec<E> elementCodec) {
        Codec<Map<RegistryKey<E>, E>> codec = RegistryCodecs.registryMap(registryRef, elementCodec);
        Encoder<Registry<E>> encoder = codec.comap(registry -> ImmutableMap.copyOf(registry.getEntrySet()));
        return Codec.of(encoder, RegistryCodecs.createRegistryDecoder(registryRef, elementCodec, codec, lifecycle), "DataPackRegistryCodec for " + registryRef);
    }

    private static <E> Decoder<Registry<E>> createRegistryDecoder(final RegistryKey<? extends Registry<E>> registryRef, final Codec<E> codec, Decoder<Map<RegistryKey<E>, E>> entryMapDecoder, Lifecycle lifecycle) {
        final Decoder<MutableRegistry<E>> decoder = entryMapDecoder.map(map -> {
            SimpleRegistry<E> mutableRegistry = new SimpleRegistry<>(registryRef, lifecycle, null);
            map.forEach((key, value) -> mutableRegistry.add(key, value, lifecycle));
            return mutableRegistry;
        });
        return new Decoder<>() {

            @Override
            public <T> DataResult<Pair<Registry<E>, T>> decode(DynamicOps<T> ops, T input) {
                DataResult<Pair<MutableRegistry<E>, T>> dataResult = decoder.decode(ops, input);
                if (ops instanceof RegistryOps<T> registryOps) {
                    return registryOps.getLoaderAccess().map(loaderAccess -> this.load(dataResult, registryOps, loaderAccess.loader())).orElseGet(() -> DataResult.error("Can't load registry with this ops"));
                }
                return dataResult.map(pair -> pair.mapFirst(registry -> registry));
            }

            private <T> DataResult<Pair<Registry<E>, T>> load(DataResult<Pair<MutableRegistry<E>, T>> result, RegistryOps<?> ops, RegistryLoader loader) {
                return result.flatMap(pair -> loader.load(pair.getFirst(), registryRef, codec, ops.getEntryOps()).map(registry -> Pair.of(registry, pair.getSecond())));
            }
        };
    }

    private static <T> Codec<Map<RegistryKey<T>, T>> registryMap(RegistryKey<? extends Registry<T>> registryRef, Codec<T> elementCodec) {
        return Codec.unboundedMap(Identifier.CODEC.xmap(RegistryKey.createKeyFactory(registryRef), RegistryKey::getValue), elementCodec);
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

    record RegistryManagerEntry<T>(RegistryKey<T> key, int rawId, T value) { }
}

