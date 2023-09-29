package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biome.VoronoiBiomeProvider;

public class NetherBiomeProviderImpl extends VoronoiBiomeProvider {
	private static final NetherBiomeProviderImpl INSTANCE = new NetherBiomeProviderImpl();
	
	private NetherBiomeProviderImpl() {
		addBiome(Biome.NETHER);
	}
	
	public static NetherBiomeProviderImpl getInstance() {
		return INSTANCE;
	}
}
