package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeColorProvider;

import java.util.function.Function;

public class ColorInterpolator {
	private final Function<Biome, BiomeColorProvider> provider;
	private final int[] colors = new int[4];
	private final int bitShift;
	private final int side;
	
	private boolean initiated;
	private int lastX;
	private int lastZ;
	
	public ColorInterpolator(Function<Biome, BiomeColorProvider> provider, int side) {
		this.side = side;
		this.bitShift = MathHelper.floor(Math.log(side) / Math.log(2));
		this.provider = provider;
	}
	
	public int getColor(BiomeSource source, double x, double z) {
		int x1 = MathHelper.floor(x / side);
		int z1 = MathHelper.floor(z / side);
		
		float dx = (float) (x / side - x1);
		float dz = (float) (z / side - z1);
		
		if (!initiated || x1 != lastX || z1 != lastZ) {
			initiated = true;
			lastX = x1;
			lastZ = z1;
			
			x1 <<= bitShift;
			z1 <<= bitShift;
			
			int x2 = x1 + side;
			int z2 = z1 + side;
			
			colors[0] = provider.apply(source.getBiome(x1, z1)).getColor(source, x1, z1);
			colors[1] = provider.apply(source.getBiome(x2, z1)).getColor(source, x2, z1);
			colors[2] = provider.apply(source.getBiome(x1, z2)).getColor(source, x1, z2);
			colors[3] = provider.apply(source.getBiome(x2, z2)).getColor(source, x2, z2);
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
		
		r1 = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, r1, r2);
		g1 = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, g1, g2);
		b1 = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, b1, b2);
		
		int ir = (int) (r1 * 255);
		int ig = (int) (g1 * 255);
		int ib = (int) (b1 * 255);
		
		return 0xFF000000 | ir << 16 | ig << 8 | ib;
	}
}
