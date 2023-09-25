package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.FixedBiomeSource;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeProvider;

public class NetherBiomeSourceImpl extends FixedBiomeSource {
	private static final NetherBiomeSourceImpl INSTANCE = new NetherBiomeSourceImpl();
	
	private NetherBiomeSourceImpl() {
		super(Biome.NETHER, 1.0, 0.0);
	}
	
	@Override
	public Biome[] getBiomes(Biome[] data, int x, int z, int dx, int dz) {
		data = super.getBiomes(data, x, z, dx, dz);
		
		BiomeProvider provider = BiomeAPI.getNetherProvider();
		
		int index = 0;
		for (int bx = 0; bx < dx; bx++) {
			for (int bz = 0; bz < dz; bz++) {
				data[index++] = provider.getBiome(x + bx, z + bz, 1.0F, 0.0F);
			}
		}
		
		return data;
	}
	
	public static NetherBiomeSourceImpl getInstance() {
		return INSTANCE;
	}
}
