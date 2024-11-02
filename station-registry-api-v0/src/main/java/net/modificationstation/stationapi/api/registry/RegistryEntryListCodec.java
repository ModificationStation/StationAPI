package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistryEntryListCodec<E> implements Codec<RegistryEntryList<E>> {
    private final RegistryKey<? extends Registry<E>> registry;
    private final Codec<RegistryEntry<E>> entryCodec;
    private final Codec<List<RegistryEntry<E>>> directEntryListCodec;
    private final Codec<Either<TagKey<E>, List<RegistryEntry<E>>>> entryListStorageCodec;

    /**
     * @param alwaysSerializeAsList whether to always serialize the list as a list
     * instead of serializing as one entry if the length is {@code 0}
     */
    private static <E> Codec<List<RegistryEntry<E>>> createDirectEntryListCodec(Codec<RegistryEntry<E>> entryCodec, boolean alwaysSerializeAsList) {
        Codec<List<RegistryEntry<E>>> codec = Codecs.validate(entryCodec.listOf(), Codecs.createEqualTypeChecker(RegistryEntry::getType));
        if (alwaysSerializeAsList) return codec;
        return Codec.either(codec, entryCodec).xmap(either -> either.map(entries -> entries, List::of), entries -> entries.size() == 1 ? Either.right(entries.get(0)) : Either.left(entries));
    }

    /**
     * @param alwaysSerializeAsList whether to always serialize the list as a list
     * instead of serializing as one entry if the length is {@code 0}
     */
    public static <E> Codec<RegistryEntryList<E>> create(RegistryKey<? extends Registry<E>> registryRef, Codec<RegistryEntry<E>> entryCodec, boolean alwaysSerializeAsList) {
        return new RegistryEntryListCodec<>(registryRef, entryCodec, alwaysSerializeAsList);
    }

    /**
     * @param alwaysSerializeAsList whether to always serialize the list as a list
     * instead of serializing as one entry if the length is {@code 0}
     */
    private RegistryEntryListCodec(RegistryKey<? extends Registry<E>> registry, Codec<RegistryEntry<E>> entryCodec, boolean alwaysSerializeAsList) {
        this.registry = registry;
        this.entryCodec = entryCodec;
        this.directEntryListCodec = RegistryEntryListCodec.createDirectEntryListCodec(entryCodec, alwaysSerializeAsList);
        this.entryListStorageCodec = Codec.either(TagKey.codec(registry), this.directEntryListCodec);
    }

    @Override
    public <T> DataResult<Pair<RegistryEntryList<E>, T>> decode(DynamicOps<T> ops, T input) {
        Optional<RegistryEntryLookup<E>> optional;
        if (ops instanceof RegistryOps<?> registryOps && (optional = registryOps.getEntryLookup(this.registry)).isPresent()) {
            RegistryEntryLookup<E> registryEntryLookup = optional.get();
            return this.entryListStorageCodec.decode(ops, input).map(pair -> pair.mapFirst(either -> either.map(registryEntryLookup::getOrThrow, RegistryEntryList::of)));
        }
        return this.decodeDirect(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(RegistryEntryList<E> registryEntryList, DynamicOps<T> dynamicOps, T object) {
        Optional<RegistryEntryOwner<E>> optional;
        if (dynamicOps instanceof RegistryOps<?> registryOps && (optional = registryOps.getOwner(this.registry)).isPresent()) {
            if (!registryEntryList.ownerEquals(optional.get()))
                return DataResult.error(() -> "HolderSet " + registryEntryList + " is not valid in current registry set");
            return this.entryListStorageCodec.encode(registryEntryList.getStorage().mapRight(List::copyOf), dynamicOps, object);
        }
        return this.encodeDirect(registryEntryList, dynamicOps, object);
    }

    private <T> DataResult<Pair<RegistryEntryList<E>, T>> decodeDirect(DynamicOps<T> ops, T input) {
        return this.entryCodec.listOf().decode(ops, input).flatMap(pair -> {
            ArrayList<RegistryEntry.Direct<E>> list = new ArrayList<>();
            for (RegistryEntry<E> registryEntry : pair.getFirst()) {
                if (registryEntry instanceof RegistryEntry.Direct<E> direct) {
                    list.add(direct);
                    continue;
                }
                return DataResult.error(() -> "Can't decode element " + registryEntry + " without registry");
            }
            return DataResult.success(Pair.of(RegistryEntryList.of(list), pair.getSecond()));
        });
    }

    private <T> DataResult<T> encodeDirect(RegistryEntryList<E> entryList, DynamicOps<T> ops, T prefix) {
        return this.directEntryListCodec.encode(entryList.stream().toList(), ops, prefix);
    }
}

