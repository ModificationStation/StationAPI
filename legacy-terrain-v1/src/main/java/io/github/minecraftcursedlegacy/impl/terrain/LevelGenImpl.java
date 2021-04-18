package io.github.minecraftcursedlegacy.impl.terrain;

import io.github.minecraftcursedlegacy.api.terrain.ChunkGenEvents;
import io.github.minecraftcursedlegacy.api.terrain.ExtendedBiome;
import net.fabricmc.api.ModInitializer;
import net.minecraft.level.structure.Feature;

public class LevelGenImpl implements ModInitializer {
	@Override
	public void onInitialize() {
		// Extended Biome
		ChunkGenEvents.Decorate decoration = (level, biome, rand, x, z) -> {
			if (biome instanceof ExtendedBiome) {
				ExtendedBiome eBiome = (ExtendedBiome) biome;

				for(int i = 0; i < eBiome.getTreesPerChunk(); ++i) {
					int xToGen = x + rand.nextInt(16) + 8;
					int zToGen = z + rand.nextInt(16) + 8;
					Feature feature = biome.getTree(rand);
					feature.setupTreeGeneration(1.0D, 1.0D, 1.0D);
					feature.generate(level, rand, xToGen, level.getHeight(xToGen, zToGen), zToGen);
				}
			}
		};

		ChunkGenEvents.Decorate.OVERWORLD.register(decoration);
		ChunkGenEvents.Decorate.NETHER.register(decoration);
	}
}
