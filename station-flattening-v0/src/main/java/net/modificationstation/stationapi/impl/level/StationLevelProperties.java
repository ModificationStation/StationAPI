package net.modificationstation.stationapi.impl.level;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface StationLevelProperties {
	CompoundTag getDimensionTag(Identifier id);
}
