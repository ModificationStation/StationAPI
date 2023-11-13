package net.modificationstation.stationapi.impl.world;

import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public interface StationWorldProperties {
    NbtCompound getDimensionTag(Identifier id);
}
