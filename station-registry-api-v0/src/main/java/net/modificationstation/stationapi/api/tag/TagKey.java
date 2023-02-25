/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.modificationstation.stationapi.api.tag;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;

import java.util.Optional;

public record TagKey<T>(RegistryKey<? extends Registry<T>> registry, Identifier id) {
    private static final Interner<TagKey<?>> INTERNER = Interners.newWeakInterner();

    public static <T> Codec<TagKey<T>> unprefixedCodec(RegistryKey<? extends Registry<T>> registry) {
        return Identifier.CODEC.xmap(id -> TagKey.of(registry, id), TagKey::id);
    }

    public static <T> Codec<TagKey<T>> codec(RegistryKey<? extends Registry<T>> registry) {
        return Codec.STRING.comapFlatMap(string -> string.startsWith("#") ? Identifier.validate(string.substring(1)).map(id -> TagKey.of(registry, id)) : DataResult.error(() -> "Not a tag id"), string -> "#" + string.id);
    }

    public static <T> TagKey<T> of(RegistryKey<? extends Registry<T>> registry, Identifier id) {
        //noinspection unchecked
        return (TagKey<T>) INTERNER.intern(new TagKey<>(registry, id));
    }

    public boolean isOf(RegistryKey<? extends Registry<?>> registryRef) {
        return this.registry == registryRef;
    }

    public <E> Optional<TagKey<E>> tryCast(RegistryKey<? extends Registry<E>> registryRef) {
        //noinspection unchecked
        return this.isOf(registryRef) ? Optional.of((TagKey<E>) this) : Optional.empty();
    }

    @Override
    public String toString() {
        return "TagKey[" + this.registry.getValue() + " / " + this.id + "]";
    }
}

