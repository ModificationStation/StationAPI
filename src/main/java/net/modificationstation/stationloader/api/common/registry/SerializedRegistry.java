package net.modificationstation.stationloader.api.common.registry;


import java.util.function.Function;

public abstract class SerializedRegistry<T> extends Registry<T> {

    public SerializedRegistry(Identifier registryId) {
        super(registryId);
    }

    public <E extends T> E register(Function<Integer, E> initializer, Identifier identifier) {
        int nextSerializedId = getNextSerializedID();
        E value = initializer.apply(nextSerializedId);
        registerSerializedValue(identifier, value, nextSerializedId);
        return value;
    }

    public abstract void registerSerializedValue(Identifier identifier, T value, int serializedId);

    public abstract int getNextSerializedID();
}