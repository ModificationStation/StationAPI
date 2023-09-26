package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.gen.BiomeSource;
import net.modificationstation.stationapi.api.util.math.MathHelper;

public class ColorInterpolator {
	private static final ColorInterpolator INSTANCE = new ColorInterpolator();
	
	private final int[] colors = new int[4];
	private boolean initiated;
	private int lastX;
	private int lastZ;
	
	private ColorInterpolator() {}
	
	public int getColor(BiomeSource source, int x, int z) {
		int x1 = x >> 3;
		int z1 = z >> 3;
		
		float dx = (float) (x / 8.0 - x1);
		float dz = (float) (z / 8.0 - z1);
		
		if (!initiated || x1 != lastX || z1 != lastZ) {
			initiated = true;
			lastX = x1;
			lastZ = z1;
			
			x1 <<= 3;
			z1 <<= 3;
			
			int x2 = x1 + 8;
			int z2 = z1 + 8;
			
			colors[0] = source.getBiome(x1, z1).grassColour;
			colors[1] = source.getBiome(x2, z1).grassColour;
			colors[2] = source.getBiome(x1, z2).grassColour;
			colors[3] = source.getBiome(x2, z2).grassColour;
		}
		
		int a = lerp(colors[0], colors[1], dx);
		int b = lerp(colors[2], colors[3], dx);
		
		return lerp(a, b, dz);
	}
	
	private int lerp(int a, int b, float delta) {
		float r1 = ((a >> 16) & 255) / 255F;
		float r2 = ((b >> 16) & 255) / 255F;
		
		float g1 = ((a >> 8) & 255) / 255F;
		float g2 = ((b >> 8) & 255) / 255F;
		
		float b1 = (a & 255) / 255F;
		float b2 = (b & 255) / 255F;
		
		r1 = MathHelper.lerp(delta, r1, r2);
		g1 = MathHelper.lerp(delta, g1, g2);
		b1 = MathHelper.lerp(delta, b1, b2);
		
		int ir = (int) (r1 * 255);
		int ig = (int) (g1 * 255);
		int ib = (int) (b1 * 255);
		
		return 0xFF000000 | ir << 16 | ig << 8 | ib;
	}
	
	public static ColorInterpolator getInstance() {
		return INSTANCE;
	}
}
