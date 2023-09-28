package net.modificationstation.stationapi.api.worldgen.surface;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface SurfaceBiome {
	default void addSurfaceRule(SurfaceRule rule) {
		Util.assertImpl();
	}
	
	default void applySurfaceRules(Level level, int x, int y, int z, BlockState state) {
		Util.assertImpl();
	}
	
	default boolean noSurfaceRules() {
		return Util.assertImpl();
	}
}
