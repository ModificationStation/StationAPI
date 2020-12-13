package net.modificationstation.stationloader.api.common.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;

public abstract class SerializedRegistry<T> extends Registry<T> {

    public SerializedRegistry(Identifier registryId) {
        super(registryId);
    }

    @Override
    public @NotNull Iterator<Map.Entry<Identifier, T>> iterator() {
        if (!updating) {
            updating = true;
            update();
            updating = false;
        }
        return super.iterator();
    }

    @Override
    public Optional<T> getByIdentifier(Identifier identifier) {
        Optional<T> value = super.getByIdentifier(identifier);
        if (!updating && !value.isPresent()) {
            updating = true;
            update();
            updating = false;
            value = super.getByIdentifier(identifier);
        }
        return value;
    }

    @Override
    public Identifier getIdentifier(T value) {
        Identifier identifier = super.getIdentifier(value);
        if (!updating && identifier == null) {
            updating = true;
            update();
            updating = false;
            identifier = super.getIdentifier(value);
        }
        return identifier;
    }

    public <E extends T> E register(IntFunction<E> initializer, Identifier identifier) {
        int nextSerializedId = getNextSerializedID();
        E value = initializer.apply(nextSerializedId);
        registerSerializedValue(identifier, value, nextSerializedId);
        return value;
    }

    public abstract void registerSerializedValue(Identifier identifier, T value, int serializedId);

    public abstract int getNextSerializedID();

    protected abstract void update();

    private boolean updating = false;
}