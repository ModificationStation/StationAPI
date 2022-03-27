package net.modificationstation.stationapi.api.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class AbstractArrayBackedRegistry<T> extends AbstractSerialRegistry<T> {

    public AbstractArrayBackedRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }

    public AbstractArrayBackedRegistry(@NotNull Identifier identifier, boolean shiftSerialIDOnRegister) {
        super(identifier, shiftSerialIDOnRegister);
    }

    protected abstract T[] getBackingArray();

    @Override
    public int getSize() {
        return getBackingArray().length;
    }

    @Override
    public @NotNull Optional<T> get(int serialID) {
        try {
            return Optional.ofNullable(getBackingArray()[serialID]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}
