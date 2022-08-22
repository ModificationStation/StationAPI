package net.modificationstation.stationapi.api.registry.legacy;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractInt2ObjectMapBackedLegacyRegistry<T> extends AbstractLegacyRegistry<T> {

    public AbstractInt2ObjectMapBackedLegacyRegistry(@NotNull RegistryKey<? extends Registry<T>> key, Function<T, RegistryEntry.Reference<T>> valueToEntryFunction) {
        super(key, valueToEntryFunction);
    }

    public AbstractInt2ObjectMapBackedLegacyRegistry(@NotNull RegistryKey<? extends Registry<T>> key, boolean shiftSerialIDOnRegister, Function<T, RegistryEntry.Reference<T>> valueToEntryFunction) {
        super(key, shiftSerialIDOnRegister, valueToEntryFunction);
    }

    protected abstract Int2ObjectMap<T> getBackingInt2ObjectMap();

    @Override
    public int getSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public @NotNull Optional<T> getByLegacyId(int serialID) {
        Int2ObjectMap<T> map = getBackingInt2ObjectMap();
        return map.containsKey(serialID) ? Optional.ofNullable(map.get(serialID)) : Optional.empty();
    }
}
