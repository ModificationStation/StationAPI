package net.modificationstation.stationapi.api.worldgen.biomeprovider;

public interface ColoredBiome {
	BiomeColorProvider getGrassColor();
	BiomeColorProvider getLeavesColor();
	void setGrassColor(BiomeColorProvider provider);
	void setLeavesColor(BiomeColorProvider provider);
	
	default void setGrassColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setGrassColor((source, x, z) -> color);
	}
	
	default void setLeavesColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setLeavesColor((source, x, z) -> color);
	}
}
