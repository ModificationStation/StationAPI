package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;

public class WorldGeneratorImpl {
    private static final BiomeDataInterpolator MIN_HEIGHT_INTERPOLATOR = new BiomeDataInterpolator(Biome::getMinHeight, 16, 4, 8);
    private static final BiomeDataInterpolator MAX_HEIGHT_INTERPOLATOR = new BiomeDataInterpolator(Biome::getMaxHeight, 16, 4, 8);

    public static void updateNoise(Level level, int cx, int cz, double[] data) {
        float min = 0;
        float max = 0;
        float n = 0;
        cx <<= 4;
        cz <<= 4;
    
        BiomeSource biomeSource = level.getBiomeSource();
        int sideY = data.length / 25;
        int dx = sideY * 5;

        for (int i = 0; i < data.length; i++) {
            int y = (i % sideY);

            if (y == 0) {
                int x = ((i / dx) << 2) + cx;
                int z = (((i / sideY) % 5) << 2) + cz;
                min = MIN_HEIGHT_INTERPOLATOR.get(biomeSource, x, z) / 8F;
                max = MAX_HEIGHT_INTERPOLATOR.get(biomeSource, x, z) / 8F;
            }
            
            y += level.getBottomY();

            if (y < min) {
                float d = (min - y) * 100 + n * 10;
                data[i] = smoothMax(data[i], d, 1);
            } else if (y > max) {
                float d = (max - y) * 100 + n * 10;
                data[i] = smoothMin(data[i], d, 1);
            }
        }
    }

    private static double smoothMax(double a, double b, double k) {
        return -smoothMin(-a, -b, k);
    }

    private static double smoothMin(double a, double b, double k) {
        double h = Math.max(k - Math.abs(a - b), 0.0) / k;
        return Math.min(a, b) - h * h * k * 0.25;
    }
}
