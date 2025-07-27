package net.modificationstation.sltest.mixin;

import net.minecraft.block.Block;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.world.HeightLimitView;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSectionsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

@Mixin(OverworldChunkGenerator.class)
public class MixinOverworldLevelSource {
    @Shadow private OctavePerlinNoiseSampler perlinNoise1;
    @Shadow private World world;

    @Unique
    private ForkJoinPool customPool = new ForkJoinPool(8);

    @Inject(method = "getChunk", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/gen/chunk/OverworldChunkGenerator;buildTerrain(II[B[Lnet/minecraft/world/biome/Biome;[D)V"
    ), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onGetChunk(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> info, byte[] blocks, Chunk chunk, double[] var5) {
        short height = (short) ((HeightLimitView) world).getTopY();
        if (height < 129) return;

        BlockState stone = ((BlockStateHolder) Block.STONE).getDefaultState();
        BlockState dirt = ((BlockStateHolder) Block.DIRT).getDefaultState();
        BlockState grass = ((BlockStateHolder) Block.GRASS_BLOCK).getDefaultState();
        BlockState water = ((BlockStateHolder) Block.WATER).getDefaultState();
        BlockState gravel = ((BlockStateHolder) Block.GRAVEL).getDefaultState();

        ChunkSectionsAccessor accessor = (ChunkSectionsAccessor) chunk;
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
                section.setBlockState(i & 15, (i >> 4) & 15, (i >> 8) & 15, stone);
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
                        section.setBlockState(x, y, z, stone);
                    }

                    short dirtLevel = (short) (maxY - 3);
                    short grassLevel = (short) (maxY - 1);

                    if (map[i] < 62) {
                        if (dirtLevel >= 0 && dirtLevel < 16) {
                            for (byte y = (byte) dirtLevel; y <= grassLevel; y++) {
                                section.setBlockState(x, y, z, gravel);
                            }
                        }
                        if (waterLevel >= 0) {
                            if (waterLevel > 16) {
                                waterLevel = 16;
                            }
                            for (byte y = 0; y < waterLevel; y++) {
                                section.setBlockState(x, y, z, water);
                            }
                        }
                    }
                    else {
                        if (dirtLevel >= 0 && dirtLevel < 16) {
                            if (grassLevel > 16) {
                                grassLevel = 16;
                            }
                            for (byte y = (byte) dirtLevel; y < grassLevel; y++) {
                                section.setBlockState(x, y, z, dirt);
                            }
                        }
                        if (grassLevel >= 0 && grassLevel < 16) {
                            section.setBlockState(x, grassLevel, z, grass);
                        }
                    }
                }
            }
        }));
    }

    @Inject(method = "buildTerrain", at = @At("HEAD"), cancellable = true)
    private void disableShapeChunk(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, double[] temperatures, CallbackInfo info) {
        if (canApply()) {
            info.cancel();
        }
    }

    @Inject(method = "buildSurfaces", at = @At("HEAD"), cancellable = true)
    private void disableBuildSurface(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, CallbackInfo info) {
        if (canApply()) {
            info.cancel();
        }
    }

    @Unique
    private float getNoise(double x, double z) {
        float noise = (float) perlinNoise1.sample(x, z);
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

    @Unique
    private boolean canApply() {
        return ((HeightLimitView) world).getTopY() > 128;
    }
}
