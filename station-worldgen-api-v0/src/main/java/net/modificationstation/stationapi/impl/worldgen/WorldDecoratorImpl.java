package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.block.SandBlock;
import net.minecraft.class_153;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Random;

public class WorldDecoratorImpl {
    private static final class_153[] BIOMES = new class_153[256];
    private static final Random RANDOM = new Random();

    public static void decorate(World level, int cx, int cz) {
        SandBlock.field_375 = true;
        
        int x1 = cx << 4 | 8;
        int z1 = cz << 4 | 8;
        int x2 = x1 + 16;
        int z2 = z1 + 16;
        
        level.method_1781().method_1791(BIOMES, x1, z1, 16, 16);

        int index = 0;
        for (int x = x1; x < x2; x++) {
            for (int z = z1; z < z2; z++) {
                class_153 biome = BIOMES[index++];
                int minY = level.getBottomY();
                int maxY = level.dimension.field_2177 ? level.getTopY() : level.method_222(x, z);
                for (int y = minY; y < maxY; y++) {
                    BlockState state = level.getBlockState(x, y, z);
                    biome.applySurfaceRules(level, x, y, z, state);
                }
            }
        }
    
        class_153 biome = BIOMES[136];
    
        if (biome.getStructures().isEmpty()) return;
        
        RANDOM.setSeed(level.getSeed());
        long dx = (RANDOM.nextLong() >> 1) << 1 | 1;
        long dy = (RANDOM.nextLong() >> 1) << 1 | 1;
        RANDOM.setSeed((long) cx * dx + (long) cz * dy ^ level.getSeed());
        
        int y = level.method_222(x1, z1);
        biome.getStructures().forEach(structure -> structure.method_1142(level, RANDOM, x1, y, z1));
    }
}
