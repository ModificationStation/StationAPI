package net.modificationstation.stationloader.api.common.registry;

import net.minecraft.util.io.CompoundTag;

public abstract class LevelRegistry<T> extends SerializedRegistry<T> {

    public LevelRegistry(Identifier registryId) {
        super(registryId);
    }

    public abstract void load(CompoundTag tag);

    public abstract void save(CompoundTag tag);
}
