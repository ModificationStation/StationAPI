package io.github.minecraftcursedlegacy.api.terrain;

import java.util.Random;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;

public class ChunkGenEvents {
	/**
	 * Callback for Chunk Decoration (i.e. the placement of Ores, Trees, Lakes, Dungeons, et cetera.
	 */
	public interface Decorate {
		/**
		 * The ChunkDecorateCallback for the overworld dimension. Runs after vanilla decorations but before the frozen top layer.
		 */
		Event<Decorate> OVERWORLD = EventFactory.createArrayBacked(Decorate.class,
				(listeners) -> (level, biome, rand, x, z) -> {
					for (Decorate listener : listeners) {
						listener.onDecorate(level, biome, rand, x, z);
					}
				});

		/**
		 * The ChunkDecorateCallback for the nether dimension.
		 */
		Event<Decorate> NETHER = EventFactory.createArrayBacked(Decorate.class,
				(listeners) -> (level, biome, rand, x, z) -> {
					for (Decorate listener : listeners) {
						listener.onDecorate(level, biome, rand, x, z);
					}
				});

		/**
		 * Called on chunk decoration, after vanilla decorators are run.
		 * @param level the level to generate in.
		 * @param biome the biome of the chunk region to be considered for decoration.
		 * @param rand the worldgen pseudorandom number generator.
		 * @param x the start X position for worldgen, equal to chunkX * 16.
		 * @param z the start Z position for worldgen, equal to chunkZ * 16.
		 */
		void onDecorate(Level level, Biome biome, Random rand, int x, int z);
	}

	/**
	 * Callback for chunk shaping.
	 */
	@FunctionalInterface
	public interface Shape {
		Event<Shape> OVERWORLD = EventFactory.createArrayBacked(Shape.class,
				(listeners) -> (level, biome, rand, x, z) -> {
					for (Shape listener : listeners) {
						listener.onShape(level, biome, rand, x, z);
					}
				});

		/**
		 * Called after the chunk is shaped.
		 *
		 * @param chunkX the x position of the chunk in the chunk grid.
		 * @param chunkZ the z position of the chunk in the chunk grid.
		 * @param tiles the byte array representing the ids of the tiles in the chunk.
		 */
		void onShape(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, double[] temperatures);
	}

}
