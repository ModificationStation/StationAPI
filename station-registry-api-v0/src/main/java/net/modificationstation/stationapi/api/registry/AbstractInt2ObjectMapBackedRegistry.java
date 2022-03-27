package net.modificationstation.stationapi.api.registry;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class AbstractInt2ObjectMapBackedRegistry<T> extends AbstractSerialRegistry<T> {

    public AbstractInt2ObjectMapBackedRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }

    public AbstractInt2ObjectMapBackedRegistry(@NotNull Identifier identifier, boolean shiftSerialIDOnRegister) {
        super(identifier, shiftSerialIDOnRegister);
    }

    protected abstract Int2ObjectMap<T> getBackingInt2ObjectMap();

    @Override
    public int getSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public @NotNull Optional<T> get(int serialID) {
        Int2ObjectMap<T> map = getBackingInt2ObjectMap();
        return map.containsKey(serialID) ? Optional.ofNullable(map.get(serialID)) : Optional.empty();
    }
}
