package net.modificationstation.stationapi.api.registry.legacy;

import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class AbstractArrayBackedLegacyRegistry<T> extends AbstractLegacyRegistry<T> {

    public AbstractArrayBackedLegacyRegistry(@NotNull RegistryKey<? extends Registry<T>> key) {
        super(key);
    }

    public AbstractArrayBackedLegacyRegistry(@NotNull RegistryKey<? extends Registry<T>> key, boolean shiftSerialIDOnRegister) {
        super(key, shiftSerialIDOnRegister);
    }

    protected abstract T[] getBackingArray();

    @Override
    public int getSize() {
        return getBackingArray().length;
    }

    @Override
    public @NotNull Optional<T> getByLegacyId(int serialID) {
        try {
            return Optional.ofNullable(getBackingArray()[serialID]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}
