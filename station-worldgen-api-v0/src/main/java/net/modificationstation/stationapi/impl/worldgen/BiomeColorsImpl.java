package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeColorProvider;

public class BiomeColorsImpl {
	public static final BiomeColorProvider DEFAULT_GRASS_COLOR = (BiomeSource source, int x, int z) -> {
		source.getBiome(x, z);
		double t = source.temperatureNoises[0];
		double w = source.rainfallNoises[0];
		return GrassColour.get(t, w);
	};
	
	public static final BiomeColorProvider DEFAULT_LEAVES_COLOR = (BiomeSource source, int x, int z) -> {
		source.getBiome(x, z);
		double t = source.temperatureNoises[0];
		double w = source.rainfallNoises[0];
		return FoliageColour.method_1080(t, w);
	};
	
	public static final BiomeColorProvider DEFAULT_FOG_COLOR = (BiomeSource source, int x, int z) -> 0xFFC3DAFF;
	
	public static final ColorInterpolator GRASS_INTERPOLATOR = new ColorInterpolator(Biome::getGrassColor, 8);
	public static final ColorInterpolator LEAVES_INTERPOLATOR = new ColorInterpolator(Biome::getLeavesColor, 8);
	public static final ColorInterpolator FOG_INTERPOLATOR = new ColorInterpolator(Biome::getFogColor, 16);
}
