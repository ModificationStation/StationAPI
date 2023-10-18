package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.block.Sand;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Random;

public class WorldDecoratorImpl {
    private static final Biome[] BIOMES = new Biome[256];
    private static final Random RANDOM = new Random();

    public static void decorate(Level level, int cx, int cz) {
        Sand.fallInstantly = true;
        
        int x1 = cx << 4 | 8;
        int z1 = cz << 4 | 8;
        int x2 = x1 + 16;
        int z2 = z1 + 16;
        
        level.getBiomeSource().getBiomes(BIOMES, x1, z1, 16, 16);

        int index = 0;
        for (int x = x1; x < x2; x++) {
            for (int z = z1; z < z2; z++) {
                Biome biome = BIOMES[index++];
                int h = level.dimension.halvesMapping ? level.getHeight() : level.getHeight(x, z);
                for (int y = 0; y < h; y++) {
                    BlockState state = level.getBlockState(x, y, z);
                    biome.applySurfaceRules(level, x, y, z, state);
                }
            }
        }
    
        Biome biome = BIOMES[136];
    
        if (biome.getStructures().isEmpty()) return;
        
        RANDOM.setSeed(level.getSeed());
        long dx = (RANDOM.nextLong() >> 1) << 1 | 1;
        long dy = (RANDOM.nextLong() >> 1) << 1 | 1;
        RANDOM.setSeed((long) cx * dx + (long) cz * dy ^ level.getSeed());
        
        biome.getStructures().forEach(structure -> structure.generate(level, RANDOM, x1, 0, z1));
    }
}
