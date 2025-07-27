package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeColorProvider;

public class BiomeColorsImpl {
    public static final BiomeColorProvider DEFAULT_GRASS_COLOR = (BiomeSource source, int x, int z) -> {
        source.getBiomesInArea(x, z, 1, 1);
        double t = source.temperatureMap[0];
        double w = source.downfallMap[0];
        return GrassColors.getColor(t, w);
    };

    public static final BiomeColorProvider DEFAULT_LEAVES_COLOR = (BiomeSource source, int x, int z) -> {
        source.getBiomesInArea(x, z, 1, 1);
        double t = source.temperatureMap[0];
        double w = source.downfallMap[0];
        return FoliageColors.getColor(t, w);
    };

    public static final BiomeColorProvider DEFAULT_FOG_COLOR = (BiomeSource source, int x, int z) -> 0xFFC3DAFF;

    public static final BiomeColorInterpolator GRASS_INTERPOLATOR = new BiomeColorInterpolator(Biome::getGrassColor, 8);
    public static final BiomeColorInterpolator LEAVES_INTERPOLATOR = new BiomeColorInterpolator(Biome::getLeavesColor, 8);
    public static final BiomeColorInterpolator FOG_INTERPOLATOR = new BiomeColorInterpolator(Biome::getFogColor, 16);
}
