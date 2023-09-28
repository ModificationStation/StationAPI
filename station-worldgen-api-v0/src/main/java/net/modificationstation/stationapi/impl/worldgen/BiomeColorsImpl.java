package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeColorProvider;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.ColoredBiome;

public class BiomeColorsImpl {
	public static final BiomeColorProvider DEFAULT_GRASS_COLOR = (BiomeSource source, int x, int z) -> {
		source.getBiomes(x, z, 1, 1);
		double t = source.temperatureNoises[0];
		double w = source.rainfallNoises[0];
		return GrassColour.get(t, w);
	};
	
	public static final BiomeColorProvider DEFAULT_LEAVES_COLOR = (BiomeSource source, int x, int z) -> {
		source.getBiomes(x, z, 1, 1);
		double t = source.temperatureNoises[0];
		double w = source.rainfallNoises[0];
		return FoliageColour.method_1080(t, w);
	};
	
	public static final ColorInterpolator GRASS_INTERPOLATOR = new ColorInterpolator(Biome::getGrassColor);
	public static final ColorInterpolator LEAVES_INTERPOLATOR = new ColorInterpolator(Biome::getLeavesColor);
}
