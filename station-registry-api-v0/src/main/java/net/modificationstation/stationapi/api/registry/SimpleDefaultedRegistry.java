package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class SimpleDefaultedRegistry<T> extends SimpleRegistry<T> implements DefaultedRegistry<T> {
    private final Identifier defaultId;
    private RegistryEntry.Reference<T> defaultEntry;

    public SimpleDefaultedRegistry(Identifier defaultId, RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, boolean intrusive) {
        super(key, lifecycle, intrusive);
        this.defaultId = defaultId;
    }

    public RegistryEntry.Reference<T> set(int rawId, RegistryKey<T> registryKey, T value, Lifecycle lifecycle) {
        RegistryEntry.Reference<T> reference = super.set(rawId, registryKey, value, lifecycle);
        if (this.defaultId.equals(registryKey.getValue())) this.defaultEntry = reference;

        return reference;
    }

    public int getRawId(@Nullable T value) {
        int i = super.getRawId(value);
        return i == -1 ? super.getRawId(this.defaultEntry.value()) : i;
    }

    @NotNull
    public Identifier getId(T value) {
        Identifier identifier = super.getId(value);
        return identifier == null ? this.defaultId : identifier;
    }

    @NotNull
    public T get(@Nullable Identifier id) {
        T object = super.get(id);
        return object == null ? this.defaultEntry.value() : object;
    }

    public Optional<T> getOrEmpty(@Nullable Identifier id) {
        return Optional.ofNullable(super.get(id));
    }

    @NotNull
    public T get(int index) {
        T object = super.get(index);
        return object == null ? this.defaultEntry.value() : object;
    }

    public Optional<RegistryEntry.Reference<T>> getRandom(Random random) {
        return super.getRandom(random).or(() -> Optional.of(this.defaultEntry));
    }

    public Identifier getDefaultId() {
        return this.defaultId;
    }
}
