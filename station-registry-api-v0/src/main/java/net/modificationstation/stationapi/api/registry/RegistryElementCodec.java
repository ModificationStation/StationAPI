package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;

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
        Optional<RegistryEntryOwner<E>> optional;
        if (dynamicOps instanceof RegistryOps<?> registryOps && (optional = registryOps.getOwner(this.registryRef)).isPresent()) {
            if (!registryEntry.ownerEquals(optional.get()))
                return DataResult.error(() -> "Element " + registryEntry + " is not valid in current registry set");
            return registryEntry.getKeyOrValue().map(key -> Identifier.CODEC.encode(key.getValue(), dynamicOps, object), value -> this.elementCodec.encode(value, dynamicOps, object));
        }
        return this.elementCodec.encode(registryEntry.value(), dynamicOps, object);
    }

    @Override
    public <T> DataResult<Pair<RegistryEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
        if (ops instanceof RegistryOps<?> registryOps) {
            Optional<RegistryEntryLookup<E>> optional = registryOps.getEntryLookup(this.registryRef);
            if (optional.isEmpty()) return DataResult.error(() -> "Registry does not exist: " + this.registryRef);
            RegistryEntryLookup<E> registryEntryLookup = optional.get();
            DataResult<Pair<Identifier, T>> dataResult = Identifier.CODEC.decode(ops, input);
            if (dataResult.result().isEmpty())
                return !this.allowInlineDefinitions ? DataResult.error(() -> "Inline definitions not allowed here") : this.elementCodec.decode(ops, input).map(pairx -> pairx.mapFirst(RegistryEntry::of));
            Pair<Identifier, T> pair = dataResult.result().get();
            RegistryKey<E> registryKey = RegistryKey.of(this.registryRef, pair.getFirst());
            return registryEntryLookup
                    .getOptional(registryKey)
                    .<DataResult<RegistryEntry<E>>>map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Failed to get element " + registryKey))
                    .map(reference -> Pair.of(reference, pair.getSecond()))
                    .setLifecycle(Lifecycle.stable());
        }
        return this.elementCodec.decode(ops, input).map(pairx -> pairx.mapFirst(RegistryEntry::of));
    }

    @Override
    public String toString() {
        return "RegistryFileCodec[" + this.registryRef + " " + this.elementCodec + "]";
    }
}

