package net.modificationstation.sltest.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.OverworldLevelSource;
import net.minecraft.util.noise.PerlinOctaveNoise;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OverworldLevelSource.class)
public class MixinOverworldLevelSource {
	@Shadow private PerlinOctaveNoise interpolationNoise;
	@Shadow private Level level;
	
	@Unique
	private ChunkSection stoneSection;
	
	@Inject(method = "getChunk(II)Lnet/minecraft/level/chunk/Chunk;", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/source/OverworldLevelSource;shapeChunk(II[B[Lnet/minecraft/level/biome/Biome;[D)V"
	), locals = LocalCapture.CAPTURE_FAILHARD)
	private void onGetChunk(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> info, byte[] blocks, Chunk chunk, double[] var5) {
		short height = StationLevelProperties.class.cast(level.getProperties()).getLevelHeight();
		if (height < 129) return;
		
		BlockState stone = BlockStateHolder.class.cast(BlockBase.STONE).getDefaultState();
		BlockState dirt = BlockStateHolder.class.cast(BlockBase.DIRT).getDefaultState();
		BlockState grass = BlockStateHolder.class.cast(BlockBase.GRASS).getDefaultState();
		BlockState water = BlockStateHolder.class.cast(BlockBase.STILL_WATER).getDefaultState();
		BlockState gravel = BlockStateHolder.class.cast(BlockBase.GRAVEL).getDefaultState();
		BlockStateView view = BlockStateView.class.cast(chunk);
		
		if (stoneSection == null) {
			stoneSection = new ChunkSection(0);
			for (short i = 0; i < 4096; i++) {
				stoneSection.setBlockState(i & 15, (i >> 4) & 15, (i >> 8) & 15, stone);
			}
		}
		
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
		
		for (short y = 0; y < minSection; y++) {
			ChunkSection section = sections[y];
			for (short i = 0; i < 4096; i++) {
				section.setBlockState(i & 15, (i >> 4) & 15, (i >> 8) & 15, stone);
			}
		}
		
		short startY = (short) (minSection << 4);
		for (short i = 0; i < 256; i++) {
			byte x = (byte) (i & 15);
			byte z = (byte) (i >> 4);
			short h = map[i];
			short dirtLevel = (short) (h - 3);
			short grassLevel = (short) (h - 1);
			
			for (short y = startY; y < dirtLevel; y++) {
				view.setBlockState(x, y, z, stone);
			}
			if (h < 62) {
				for (short y = dirtLevel; y <= grassLevel; y++) {
					view.setBlockState(x, y, z, gravel);
				}
			}
			else {
				for (short y = dirtLevel; y < grassLevel; y++) {
					view.setBlockState(x, y, z, dirt);
				}
				view.setBlockState(x, grassLevel, z, grass);
			}
		}
	}
	
	@Inject(method = "shapeChunk(II[B[Lnet/minecraft/level/biome/Biome;[D)V", at = @At("HEAD"), cancellable = true)
	private void disableShapeChunk(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, double[] temperatures, CallbackInfo info) {
		info.cancel();
	}
	
	@Inject(method = "buildSurface(II[B[Lnet/minecraft/level/biome/Biome;)V", at = @At("HEAD"), cancellable = true)
	private void disableBuildSurface(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, CallbackInfo info) {
		info.cancel();
	}
	
	@Unique
	private float getNoise(double x, double z) {
		float noise = (float) interpolationNoise.sample(x, z);
		return (noise + 150.0F) / 300.0F;
	}
	
	@Unique
	private short[] makeHeightmap(int wx, int wz, int delta) {
		short[] map = new short[256];
		for (short i = 0; i < 256; i++) {
			int px = wx | (i & 15);
			int pz = wz | (i >> 4);
			float noise = getNoise(px * 0.1, pz * 0.1);
			noise += getNoise(px * 0.5, pz * 0.5) * 0.25F;
			map[i] = (short) (noise * delta + 32);
		}
		return map;
	}
}
