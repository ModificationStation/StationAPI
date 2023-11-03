package net.modificationstation.stationapi.impl.level;

import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface StationLevelProperties {
    NbtCompound getDimensionTag(Identifier id);
}
