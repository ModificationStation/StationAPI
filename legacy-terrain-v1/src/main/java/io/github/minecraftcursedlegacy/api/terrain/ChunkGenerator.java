package io.github.minecraftcursedlegacy.api.terrain;

import java.util.Random;

import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.gen.OverworldCave;
import net.minecraft.level.source.LevelSource;
import net.minecraft.util.ProgressListener;

/**
 * A level source that represents a simple chunk generator template for a world type or dimension.
 * @since 1.0.0
 */
public abstract class ChunkGenerator implements LevelSource {
	public ChunkGenerator(Level level, long seed) {
		this.level = level;
		this.rand = new Random(seed);
	}

	protected final Level level;
	protected final Random rand;
	private Biome[] biomeCache;

	protected int getIndex(int localX, int y, int localZ) {
		return (localX * 16 + localZ) * 128 + y;
	}

	/**
	 * Creates the base shape of the chunk.
	 * @param chunkX the chunk X position.
	 * @param chunkZ the chunk Z position.
	 * @param tiles the array of tiles in the chunk. The index from the local x, y, z positions is specified by {@linkplain getIndex}.
	 * @param biomes the array of biomes in the chunk. Only requires an x,z index equal to {@code localX * 16 + localZ}.
	 */
	protected abstract void shapeChunk(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes);

	/**
	 * Takes the base shape of the chunk and populates it with specific terrain blocks. For example, adding a bedrock floor, and setting the top to use dirt and grass.
	 * @param chunkX the chunk X position.
	 * @param chunkZ the chunk Z position.
	 * @param tiles the array of tiles in the chunk. The index from the local x, y, z positions is specified by {@linkplain getIndex}.
	 * @param biomes the array of biomes in the chunk. Only requires an x,z index equal to {@code localX * 16 + localZ}.
	 */
	protected abstract void buildSurface(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes);

	/**
	 * Takes the shape of the chunk after shaping the chunk and building the surface, and generates carvers, such as {@linkplain OverworldCave caves} and ravines.
	 * @param chunkX the chunk X position.
	 * @param chunkZ the chunk Z position.
	 * @param tiles the array of tiles in the chunk. The index from the local x, y, z positions is specified by {@linkplain getIndex}.
	 * @param biomes the array of biomes in the chunk. Only requires an x,z index equal to {@code localX * 16 + localZ}.
	 */
	protected void generateCarvers(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes) {
	}

	@Override
	public Chunk loadChunk(int x, int z) {
		return this.getChunk(x, z);
	}

	@Override
	public Chunk getChunk(int x, int z) {
		// Setup
		this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
		byte[] tiles = new byte[128 * 16 * 16]; // HEIGHT * WIDTH * LENGTH
		Chunk result = new Chunk(this.level, tiles, x, z);

		// Biome Gen
		this.biomeCache = this.level.getBiomeSource().getBiomes(this.biomeCache, x * 16, z * 16, 16, 16);

		// Base Chunk Gen
		this.shapeChunk(x, z, tiles, this.biomeCache);
		this.buildSurface(x, z, tiles, this.biomeCache);
		this.generateCarvers(x, z, tiles, this.biomeCache);

		// Finish
		result.generateHeightmap();
		return result;
	}

	// THE FOLLOWING METHODS ARE FOR CHUNK CACHES, AND DO NOT REQUIRE LOGIC IN CHUNK GENERATORS.

	/**
	 * In a chunk generator, this always returns true, as this method is only required for level sources which load and cache chunks (i.e. Chunk Caches).
	 */
	@Override
	public final boolean isChunkLoaded(int chunkX, int chunkZ) {
		return true;
	}

	@Override
	public final boolean method_1804(boolean flag, ProgressListener arg) {
		return true;
	}

	@Override
	public final boolean method_1801() {
		return false;
	}

	@Override
	public final boolean method_1805() {
		return true;
	}

}
