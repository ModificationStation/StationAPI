package net.modificationstation.stationloader.api.common.registry;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.StationLoader;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    public final Identifier getRegistryId() {
        return registryId;
    }

    private final Identifier registryId;

    private final Map<Identifier, T> ID_TO_TYPE = new TreeMap<>();
    private final Map<T, Identifier> TYPE_TO_ID = new HashMap<>();

    public static final Registry<Registry<?>> REGISTRIES;
    public static final SerializedRegistry<BlockBase> BLOCKS;
    static {
        ModID sl = ModID.of(StationLoader.INSTANCE);
        REGISTRIES = new RegistryRegistry(Identifier.of(sl, "registries"));
        //noinspection StaticInitializerReferencesSubClass
        BLOCKS = new BlockRegistry(Identifier.of(sl, "blocks"));
    }

    private static final class RegistryRegistry extends Registry<Registry<?>> {

        public RegistryRegistry(Identifier registryId) {
            super(registryId, false);
            registerValue(registryId, this);
        }

        @Override
        public int getRegistrySize() {
            return Integer.MAX_VALUE;
        }
    }

    private static final class BlockRegistry extends SerializedRegistry<BlockBase> {

        public BlockRegistry(Identifier registryId) {
            super(registryId);
        }

        @Override
        public void registerSerializedValue(Identifier identifier, BlockBase value, int serializedId) {
            registerValue(identifier, value);
            BlockBase[] BY_ID = BlockBase.BY_ID;
            if (BY_ID[serializedId] == null)
                BY_ID[serializedId] = value;
        }

        @Override
        public int getNextSerializedID() {
            BlockBase[] BY_ID = BlockBase.BY_ID;
            for (int i = 0; i < BY_ID.length; i++)
                if (BY_ID[i] == null)
                    return i;
            throw new RuntimeException("No free space left!");
        }

        @Override
        public int getRegistrySize() {
            return BlockBase.BY_ID.length;
        }
    }
}
