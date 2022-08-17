package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.util.dynamic.RegistryLoader;
import net.modificationstation.stationapi.api.util.dynamic.RegistryOps;

import java.util.Optional;

/**
 * A codec for registry elements. Will prefer to encode/decode objects as
 * identifiers if they exist in a registry and falls back to full encoding/
 * decoding behavior if it cannot do so.
 * 
 * <p>The codec's saves and loads {@code Supplier<E>} in order to avoid early
 * loading from registry before a registry is fully loaded from a codec.
 * 
 * @param <E> the element type
 * @see RegistryOps
 */
public final class RegistryElementCodec<E> implements Codec<RegistryEntry<E>> {
    private final RegistryKey<? extends Registry<E>> registryRef;
    private final Codec<E> elementCodec;
    private final boolean allowInlineDefinitions;

    public static <E> RegistryElementCodec<E> of(RegistryKey<? extends Registry<E>> registryRef, Codec<E> elementCodec) {
        return RegistryElementCodec.of(registryRef, elementCodec, true);
    }

    public static <E> RegistryElementCodec<E> of(RegistryKey<? extends Registry<E>> registryRef, Codec<E> elementCodec, boolean allowInlineDefinitions) {
        return new RegistryElementCodec<>(registryRef, elementCodec, allowInlineDefinitions);
    }

    private RegistryElementCodec(RegistryKey<? extends Registry<E>> registryRef, Codec<E> elementCodec, boolean allowInlineDefinitions) {
        this.registryRef = registryRef;
        this.elementCodec = elementCodec;
        this.allowInlineDefinitions = allowInlineDefinitions;
    }

    @Override
    public <T> DataResult<T> encode(RegistryEntry<E> registryEntry, DynamicOps<T> dynamicOps, T object) {
        Optional<? extends Registry<E>> optional;
        if (dynamicOps instanceof RegistryOps<T> rOps && (optional = rOps.getRegistry(this.registryRef)).isPresent()) {
            if (!registryEntry.matchesRegistry(optional.get())) {
                return DataResult.error("Element " + registryEntry + " is not valid in current registry set");
            }
            return registryEntry.getKeyOrValue().map(key -> Identifier.CODEC.encode(key.getValue(), dynamicOps, object), value -> this.elementCodec.encode(value, dynamicOps, object));
        }
        return this.elementCodec.encode(registryEntry.value(), dynamicOps, object);
    }

    @Override
    public <T> DataResult<Pair<RegistryEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
        if (ops instanceof RegistryOps<T> registryOps) {
            Optional<? extends Registry<E>> optional = registryOps.getRegistry(this.registryRef);
            if (optional.isEmpty()) {
                return DataResult.error("Registry does not exist: " + this.registryRef);
            }
            Registry<E> registry = optional.get();
            DataResult<Pair<Identifier, T>> dataResult = Identifier.CODEC.decode(ops, input);
            if (dataResult.result().isEmpty()) {
                if (!this.allowInlineDefinitions) {
                    return DataResult.error("Inline definitions not allowed here");
                }
                return this.elementCodec.decode(ops, input).map(pair -> pair.mapFirst(RegistryEntry::of));
            }
            Pair<Identifier, T> pair2 = dataResult.result().get();
            RegistryKey<E> registryKey = RegistryKey.of(this.registryRef, pair2.getFirst());
            Optional<RegistryLoader.LoaderAccess> optional2 = registryOps.getLoaderAccess();
            if (optional2.isPresent()) {
                return optional2.get().load(this.registryRef, this.elementCodec, registryKey, registryOps.getEntryOps()).map(entry -> Pair.of(entry, pair2.getSecond()));
            }
            DataResult<RegistryEntry<E>> dataResult2 = registry.getOrCreateEntryDataResult(registryKey);
            return dataResult2.map(entry -> Pair.of(entry, pair2.getSecond())).setLifecycle(Lifecycle.stable());
        }
        return this.elementCodec.decode(ops, input).map(pair -> pair.mapFirst(RegistryEntry::of));
    }

    public String toString() {
        return "RegistryFileCodec[" + this.registryRef + " " + this.elementCodec + "]";
    }
}

