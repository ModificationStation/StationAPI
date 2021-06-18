package net.modificationstation.stationapi.api.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.IntFunction;

public abstract class AbstractSerialRegistry<T> extends Registry<T> {

    public AbstractSerialRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }

    public abstract int getSize();

    public abstract int getSerialID(T value);

    public int getSerialID(Identifier identifier) {
        return get(identifier).map(this::getSerialID).orElse(-1);
    }

    public abstract Optional<T> get(int serialID);

    public abstract int getFirstSerialID();

    public int getNextSerialID() {
        for (int i = getFirstSerialID(); i < getSize(); i++) if (!get(i).isPresent())
            return i;
        throw new IndexOutOfBoundsException("No more free serial IDs left for " + getRegistryId() + " registry!");
    }

    public <E extends T> E register(Identifier identifier, IntFunction<E> initializer) {
        int serialID = getNextSerialID();
        E value = initializer.apply(serialID);
        register(identifier, value);
        return value;
    }
}
