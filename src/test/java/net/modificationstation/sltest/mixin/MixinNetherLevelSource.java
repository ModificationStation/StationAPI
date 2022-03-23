package net.modificationstation.sltest.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.NetherLevelSource;
import net.minecraft.util.noise.PerlinOctaveNoise;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.impl.level.StationDimension;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

@Mixin(NetherLevelSource.class)
public class MixinNetherLevelSource {
	@Shadow private PerlinOctaveNoise field_1347;
	@Shadow private Level level;
	
	@Unique
	private ForkJoinPool customPool = new ForkJoinPool(8);
	
	@Inject(method = "getChunk(II)Lnet/minecraft/level/chunk/Chunk;", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void onGetChunk(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> info, byte[] blocks, Chunk chunk) {
		StationDimension dimension = StationDimension.class.cast(level.dimension);
		short height = dimension.getActualLevelHeight();
		if (height == 128) return;
		
		BlockState netherrack = BlockStateHolder.class.cast(BlockBase.NETHERRACK).getDefaultState();
		BlockState lava = BlockStateHolder.class.cast(BlockBase.STILL_LAVA).getDefaultState();
		
		ChunkSectionsAccessor accessor = ChunkSectionsAccessor.class.cast(chunk);
		ChunkSection[] sections = accessor.getSections();
		
		short max = 0;
		short min = height;
		short[] map = makeHeightmap(chunkX << 4, chunkZ << 4, height - 40);
		for (short i = 0; i < 256; i++) {
			if (map[i] > max) {
				max = map[i];
			}
			if (map[i] < min) {
				min = map[i];
			}
		}
		
		short minSection = (short) ((min - 16) >> 4);
		short maxSection = (short) ((max + 16) >> 4);
		if (minSection < 0) {
			minSection = 0;
		}
		if (maxSection >= sections.length) {
			maxSection = (short) (sections.length - 1);
		}
		
		for (short y = 0; y < maxSection; y++) {
			if (sections[y] == null) {
				sections[y] = new ChunkSection(y << 4);
			}
		}
		
		final int finalMin = minSection;
		final int finalMax = maxSection;
		customPool.submit(() -> IntStream.range(0, finalMin).parallel().forEach(n -> {
			ChunkSection section = sections[n];
			for (short i = 0; i < 4096; i++) {
				section.setBlockState(i & 15, (i >> 4) & 15, (i >> 8) & 15, netherrack, false);
			}
		}));
		
		customPool.submit(() -> IntStream.range(finalMin, finalMax).parallel().forEach(n -> {
			ChunkSection section = sections[n];
			for (short i = 0; i < 256; i++) {
				byte x = (byte) (i & 15);
				byte z = (byte) (i >> 4);
				short maxY = (short) (map[i] - section.getYOffset());
				short waterLevel = (short) (62 - section.getYOffset());
				
				if (maxY > 0 || waterLevel > 0) {
					if (maxY > 16) {
						maxY = 16;
					}
					
					for (short y = 0; y < maxY; y++) {
						section.setBlockState(x, y, z, netherrack, false);
					}
					
					/*if (waterLevel >= 0) {
						if (waterLevel > 16) {
							waterLevel = 16;
						}
						for (byte y = 0; y < waterLevel; y++) {
							section.setBlockState(x, y, z, lava, false);
						}
					}*/
				}
			}
		}));
	}
	
	@Unique
	private float getNoise(double x, double z) {
		float noise = (float) field_1347.sample(x, z);
		return (noise + 128) / 256F;
	}
	
	@Unique
	private short[] makeHeightmap(int wx, int wz, int delta) {
		short[] map = new short[256];
		for (short i = 0; i < 256; i++) {
			int px = wx | (i & 15);
			int pz = wz | (i >> 4);
			float noise = getNoise(px * 0.1, pz * 0.1);
			noise += getNoise(px * 0.5, pz * 0.5) * 0.25F;
			map[i] = (short) (noise * delta + 8);
		}
		return map;
	}
	
	@Unique
	private boolean canApply() {
		StationDimension dimension = StationDimension.class.cast(level.dimension);
		return dimension.getActualLevelHeight() != 128;
	}
}
