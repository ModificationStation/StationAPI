package io.github.minecraftcursedlegacy.test;

import io.github.minecraftcursedlegacy.api.terrain.ChunkGenerator;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.source.OverworldLevelSource;
import net.minecraft.tile.Tile;

public class RandomChunkGenerator extends ChunkGenerator {
	public RandomChunkGenerator(Level level, long seed) {
		super(level, seed);
		this.surface = new OverworldLevelSource(level, seed);
	}

	private final OverworldLevelSource surface;

	@Override
	public void decorate(LevelSource levelSource, int chunkX, int chunkZ) {
	}

	@Override
	protected void shapeChunk(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes) {
		for (int localX = 0; localX < 16; ++localX) {
			for (int localZ = 0; localZ < 16; ++localZ) {
				int height = 61 + this.rand.nextInt(6) + this.rand.nextInt(3) + ((chunkX + chunkZ) & 0b11);

				if (height < 63) {
					for (int y = 63; y > height; --y) {
						tiles[getIndex(localX, y, localZ)] = (byte) Tile.STILL_WATER.id;
					}
				}

				for (int y = height; y >= 0; --y) {
					tiles[getIndex(localX, y, localZ)] = (byte) Tile.STONE.id;
				}
			}
		}
	}

	@Override
	protected void buildSurface(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes) {
		this.surface.buildSurface(chunkX, chunkZ, tiles, biomes);
	}
}
