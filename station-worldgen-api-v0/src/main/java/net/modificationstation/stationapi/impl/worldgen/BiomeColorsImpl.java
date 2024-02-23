package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.class_287;
import net.minecraft.class_334;
import net.minecraft.class_519;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeColorProvider;

public class BiomeColorsImpl {
    public static final BiomeColorProvider DEFAULT_GRASS_COLOR = (class_519 source, int x, int z) -> {
        source.method_1788(x, z, 1, 1);
        double t = source.field_2235[0];
        double w = source.field_2236[0];
        return class_287.method_981(t, w);
    };

    public static final BiomeColorProvider DEFAULT_LEAVES_COLOR = (class_519 source, int x, int z) -> {
        source.method_1788(x, z, 1, 1);
        double t = source.field_2235[0];
        double w = source.field_2236[0];
        return class_334.method_1080(t, w);
    };

    public static final BiomeColorProvider DEFAULT_FOG_COLOR = (class_519 source, int x, int z) -> 0xFFC3DAFF;

    public static final BiomeColorInterpolator GRASS_INTERPOLATOR = new BiomeColorInterpolator(Biome::getGrassColor, 8);
    public static final BiomeColorInterpolator LEAVES_INTERPOLATOR = new BiomeColorInterpolator(Biome::getLeavesColor, 8);
    public static final BiomeColorInterpolator FOG_INTERPOLATOR = new BiomeColorInterpolator(Biome::getFogColor, 16);
}
