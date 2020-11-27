package net.modificationstation.stationloader.api.common.registry;

import net.modificationstation.stationloader.api.common.StationLoader;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public abstract class Registry<T> implements Iterable<Map.Entry<Identifier, T>> {

    public Registry(Identifier identifier) {
        this(identifier, true);
    }

    private Registry(Identifier identifier, boolean register) {
        this.registryId = identifier;
        if (register)
            REGISTRIES.registerValue(registryId, this);
    }

    public void registerValue(Identifier identifier, T value) {
        ID_TO_TYPE.put(identifier, value);
        TYPE_TO_ID.put(value, identifier);
    }

    public abstract int getRegistrySize();

    public T getByIdentifier(Identifier identifier) {
        return ID_TO_TYPE.get(identifier);
    }

    public Identifier getIdentifier(T value) {
        return TYPE_TO_ID.get(value);
    }

    @Override
    public @NotNull Iterator<Map.Entry<Identifier, T>> iterator() {
        return ID_TO_TYPE.entrySet().iterator();
    }

    @NotNull
    public final Identifier getRegistryId() {
        return registryId;
    }

    private final Identifier registryId;

    private final Map<Identifier, T> ID_TO_TYPE = new TreeMap<>();
    private final Map<T, Identifier> TYPE_TO_ID = new HashMap<>();

    public static final Registry<Registry<?>> REGISTRIES = new RegistryRegistry(Identifier.of(ModID.of(StationLoader.INSTANCE), "registries"));

    private static final class RegistryRegistry extends Registry<Registry<?>> {

        private RegistryRegistry(Identifier registryId) {
            super(registryId, false);
            registerValue(registryId, this);
        }

        @Override
        public int getRegistrySize() {
            return Integer.MAX_VALUE;
        }
    }
}
