package net.modificationstation.stationapi.api.registry;

import net.minecraft.util.io.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class LevelSerialRegistry<T> extends AbstractSerialRegistry<T> {

    public LevelSerialRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }

    public void save(CompoundTag tag) {
        forEach((identifier, value) -> tag.put(identifier.toString(), getSerialID(value)));
    }

    public void load(CompoundTag tag) {
        forEach((identifier, t) -> {
            String id = identifier.toString();
            if (tag.containsKey(id))
                remap(tag.getInt(id), t);
        });
    }

    protected abstract void remap(int newSerialID, T value);
}
