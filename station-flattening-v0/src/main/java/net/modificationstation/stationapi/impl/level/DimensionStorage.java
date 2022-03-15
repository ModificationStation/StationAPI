package net.modificationstation.stationapi.impl.level;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DimensionStorage {
	private static final Map<Identifier, CompoundTag> DIMENSION_DATA = new HashMap<>(8);
	
	public static void clear() {
		DIMENSION_DATA.clear();
	}
	
	public static CompoundTag getData(Identifier id) {
		return DIMENSION_DATA.getOrDefault(id, new CompoundTag());
	}
}
