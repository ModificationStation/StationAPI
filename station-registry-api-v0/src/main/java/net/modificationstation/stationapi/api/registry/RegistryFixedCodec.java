package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Optional;

public final class RegistryFixedCodec<E> implements Codec<RegistryEntry<E>> {
    private final RegistryKey<? extends Registry<E>> registry;

    public static <E> RegistryFixedCodec<E> of(RegistryKey<? extends Registry<E>> registry) {
        return new RegistryFixedCodec<>(registry);
    }

    private RegistryFixedCodec(RegistryKey<? extends Registry<E>> registry) {
        this.registry = registry;
    }

    @Override
    public <T> DataResult<T> encode(RegistryEntry<E> registryEntry, DynamicOps<T> dynamicOps, T object) {
        Optional<RegistryEntryOwner<E>> optional;
        if (dynamicOps instanceof RegistryOps<?> registryOps && (optional = registryOps.getOwner(this.registry)).isPresent()) {
            if (!registryEntry.ownerEquals(optional.get())) {
                return DataResult.error(() -> "Element " + registryEntry + " is not valid in current registry set");
            }
            return registryEntry.getKeyOrValue().map(registryKey -> Identifier.CODEC.encode(registryKey.getValue(), dynamicOps, object), value -> DataResult.error(() -> "Elements from registry " + this.registry + " can't be serialized to a value"));
        }
        return DataResult.error(() -> "Can't access registry " + this.registry);
    }

    @Override
    public <T> DataResult<Pair<RegistryEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
        Optional<RegistryEntryLookup<E>> optional;
        if (ops instanceof RegistryOps<?> registryOps && (optional = registryOps.getEntryLookup(this.registry)).isPresent()) {
            return Identifier.CODEC.decode(ops, input).flatMap(pair -> {
                Identifier identifier = pair.getFirst();
                return optional.get()
                        .getOptional(RegistryKey.of(this.registry, identifier))
                        .<DataResult<RegistryEntry<E>>>map(DataResult::success)
                        .orElseGet(() -> DataResult.error(() -> "Failed to get element " + identifier))
                        .map(reference -> Pair.of(reference, pair.getSecond()))
                        .setLifecycle(Lifecycle.stable());
            });
        }
        return DataResult.error(() -> "Can't access registry " + this.registry);
    }

    @Override
    public String toString() {
        return "RegistryFixedCodec[" + this.registry + "]";
    }
}

