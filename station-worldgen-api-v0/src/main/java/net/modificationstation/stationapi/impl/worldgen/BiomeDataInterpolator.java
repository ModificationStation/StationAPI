package net.modificationstation.stationapi.impl.worldgen;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import java.util.function.Function;

public class BiomeDataInterpolator {
    // TODO: remove when proper caching/saving is implemented
    private static final Long2ReferenceMap<Biome> GLOBAL_CACHE = new Long2ReferenceOpenHashMap<>();
    private static BiomeSource lastBiomeSource;

    private final Function<Biome, Number> provider;
    private final float[] data = new float[4];
    private final int bitShift;
    private final int side;
    private final int radius;
    private final int count;
    private final int distance;

    private boolean initiated;
    private int lastX;
    private int lastZ;

    public BiomeDataInterpolator(Function<Biome, Number> provider, int side, int radius, int distance) {
        this.side = side;
        this.radius = radius;
        this.distance = distance;
        this.count = (radius << 1 | 1) * (radius << 1 | 1);
        this.bitShift = MathHelper.floor(Math.log(side) / Math.log(2));
        this.provider = provider;
    }

    public float get(BiomeSource source, int x, int z) {
        if (lastBiomeSource != source) {
            GLOBAL_CACHE.clear();
            lastBiomeSource = source;
        }

        int x1 = x >> bitShift;
        int z1 = z >> bitShift;

        float dx = (float) (x - (x1 << bitShift)) / side;
        float dz = (float) (z - (z1 << bitShift)) / side;

        if (!initiated || x1 != lastX || z1 != lastZ) {
            initiated = true;
            lastX = x1;
            lastZ = z1;

            x1 <<= bitShift;
            z1 <<= bitShift;

            int x2 = x1 + side;
            int z2 = z1 + side;

            data[0] = getInArea(source, x1, z1);
            data[1] = getInArea(source, x2, z1);
            data[2] = getInArea(source, x1, z2);
            data[3] = getInArea(source, x2, z2);
        }

        float a = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(dx, data[0], data[1]);
        float b = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(dx, data[2], data[3]);

        return net.modificationstation.stationapi.api.util.math.MathHelper.lerp(dz, a, b);
    }

    private float getInArea(BiomeSource source, int x, int z) {
        float value = 0;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                final int lookupX = x + dx * distance;
                final int lookupZ = z + dz * distance;
                final long key = ((long) (lookupX) << 32L) | lookupZ;
                Biome biome = GLOBAL_CACHE.get(key);
                if (biome == null)
                    GLOBAL_CACHE.put(key, biome = source.getBiome(lookupX, lookupZ));
                value += provider.apply(biome).floatValue();
            }
        }
        return value / count;
    }
}
