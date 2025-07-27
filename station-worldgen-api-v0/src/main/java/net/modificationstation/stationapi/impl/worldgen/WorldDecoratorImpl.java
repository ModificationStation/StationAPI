package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.block.SandBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Random;

public class WorldDecoratorImpl {
    private static final Biome[] BIOMES = new Biome[256];
    private static final Random RANDOM = new Random();

    public static void decorate(World world, int cx, int cz) {
        SandBlock.fallInstantly = true;
        
        int x1 = cx << 4 | 8;
        int z1 = cz << 4 | 8;
        int x2 = x1 + 16;
        int z2 = z1 + 16;
        
        world.method_1781().getBiomesInArea(BIOMES, x1, z1, 16, 16);

        int index = 0;
        for (int x = x1; x < x2; x++) {
            for (int z = z1; z < z2; z++) {
                Biome biome = BIOMES[index++];
                int minY = world.getBottomY();
                int maxY = world.dimension.hasCeiling ? world.getTopY() : world.getTopY(x, z);
                for (int y = minY; y < maxY; y++) {
                    BlockState state = world.getBlockState(x, y, z);
                    biome.applySurfaceRules(world, x, y, z, state);
                }
            }
        }
    
        Biome biome = BIOMES[136];
    
        if (biome.getFeatures().isEmpty()) return;
        
        RANDOM.setSeed(world.getSeed());
        long dx = (RANDOM.nextLong() >> 1) << 1 | 1;
        long dy = (RANDOM.nextLong() >> 1) << 1 | 1;
        RANDOM.setSeed((long) cx * dx + (long) cz * dy ^ world.getSeed());
        
        int y = world.getTopY(x1, z1);
        biome.getFeatures().forEach(feature -> feature.generate(world, RANDOM, x1, y, z1));
    }
}
