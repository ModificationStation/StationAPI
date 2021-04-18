package io.github.minecraftcursedlegacy.api.terrain;

import net.minecraft.level.biome.Biome;

/**
 * Interface which can be implemented on your custom {@link Biome biomes} to add additional functionality.
 */
public interface ExtendedBiome {
	default int getTreesPerChunk() {
		return 0;
	}
}
