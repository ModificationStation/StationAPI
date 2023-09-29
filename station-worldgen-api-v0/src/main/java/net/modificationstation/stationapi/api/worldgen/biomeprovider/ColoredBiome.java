package net.modificationstation.stationapi.api.worldgen.biomeprovider;

import net.modificationstation.stationapi.api.util.Util;

public interface ColoredBiome {
	default BiomeColorProvider getGrassColor() {
		return Util.assertImpl();
	}
	
	default BiomeColorProvider getLeavesColor() {
		return Util.assertImpl();
	}
	
	default BiomeColorProvider getFogColor() {
		return Util.assertImpl();
	}
	
	default void setGrassColor(BiomeColorProvider provider) {
		Util.assertImpl();
	}
	
	default void setLeavesColor(BiomeColorProvider provider) {
		Util.assertImpl();
	}
	
	default void setFogColor(BiomeColorProvider provider) {
		Util.assertImpl();
	}
	
	default void setGrassColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setGrassColor((source, x, z) -> color);
	}
	
	default void setLeavesColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setLeavesColor((source, x, z) -> color);
	}
	
	default void setFogColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setFogColor((source, x, z) -> color);
	}
}
