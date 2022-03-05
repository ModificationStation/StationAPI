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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OverworldLevelSource.class)
public class MixinOverworldLevelSource {
	@Shadow private PerlinOctaveNoise interpolationNoise;
	@Shadow private Level level;
	
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
		
		int delta = height - 40;
		for (byte x = 0; x < 16; x++) {
			int px = chunkX << 4 | x;
			for (byte z = 0; z < 16; z++) {
				int pz = chunkZ << 4 | z;
				float noise = getNoise(px * 0.1, pz * 0.1);
				noise += getNoise(px * 0.5, pz * 0.5) * 0.25F;
				height = (short) (noise * delta + 32);
				short dirtLevel = (short) (height - 3);
				short grassLevel = (short) (height - 1);
				for (short y = 0; y < dirtLevel; y++) {
					view.setBlockState(x, y, z, stone);
				}
				if (height < 62) {
					for (short y = dirtLevel; y <= grassLevel; y++) {
						view.setBlockState(x, y, z, gravel);
					}
					for (short y = height; y < 62; y++) {
						view.setBlockState(x, y, z, water);
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
	}
	
	@Inject(method = "shapeChunk(II[B[Lnet/minecraft/level/biome/Biome;[D)V", at = @At("HEAD"), cancellable = true)
	private void disableShapeChunk(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, double[] temperatures, CallbackInfo info) {
		info.cancel();
	}
	
	@Inject(method = "buildSurface(II[B[Lnet/minecraft/level/biome/Biome;)V", at = @At("HEAD"), cancellable = true)
	private void disableBuildSurface(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, CallbackInfo info) {
		info.cancel();
	}
	
	private float getNoise(double x, double z) {
		float noise = (float) interpolationNoise.sample(x, z);
		return (noise + 150.0F) / 300.0F;
	}
}
