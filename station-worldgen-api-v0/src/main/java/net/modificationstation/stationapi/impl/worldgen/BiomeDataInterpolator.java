package net.modificationstation.stationapi.impl.worldgen;

import java.util.function.Function;
import net.minecraft.class_153;
import net.minecraft.class_519;
import net.minecraft.util.math.MathHelper;

public class BiomeDataInterpolator {
    private final Function<class_153, Number> provider;
    private final float[] data = new float[4];
    private final int bitShift;
    private final int side;
    private final int radius;
    private final int count;
    private final int distance;

    private boolean initiated;
    private int lastX;
    private int lastZ;

    public BiomeDataInterpolator(Function<class_153, Number> provider, int side, int radius, int distance) {
        this.side = side;
        this.radius = radius;
        this.distance = distance;
        this.count = (radius << 1 | 1) * (radius << 1 | 1);
        this.bitShift = MathHelper.floor(Math.log(side) / Math.log(2));
        this.provider = provider;
    }

    public float get(class_519 source, int x, int z) {
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

    private float getInArea(class_519 source, int x, int z) {
        float value = 0;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                class_153 biome = source.method_1787(x + dx * distance, z + dz * distance);
                value += provider.apply(biome).floatValue();
            }
        }
        return value / count;
    }
}
