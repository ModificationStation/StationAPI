package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;

public class WorldGeneratorImpl {
	private static final BiomeDataInterpolator MIN_HEIGHT_INTERPOLATOR = new BiomeDataInterpolator(Biome::getMinHeight, 16, 4, 8);
	private static final BiomeDataInterpolator MAX_HEIGHT_INTERPOLATOR = new BiomeDataInterpolator(Biome::getMaxHeight, 16, 4, 8);
	
	public static void updateNoise(int cx, int cz, double[] data, BiomeSource biomeSource) {
		float min = 0;
		float max = 0;
		float n = 0;
		cx <<= 4;
		cz <<= 4;
		
		for (int i = 0; i < data.length; i++) {
			int y = i % 17;
			
			if (y == 0) {
				int x = ((i / 85) << 2) + cx;
				int z = (((i / 17) % 5) << 2) + cz;
				min = MIN_HEIGHT_INTERPOLATOR.get(biomeSource, x, z) / 8F;
				max = MAX_HEIGHT_INTERPOLATOR.get(biomeSource, x, z) / 8F;
			}
			
			if (y < min) {
				float d = (min - y) * 100 + n * 10;
				data[i] = smoothMax(data[i], d, 1);
			}
			else if (y > max) {
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
