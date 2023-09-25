package net.modificationstation.stationapi.impl.worldgen;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeProvider;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeProviderRegistryEvent;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.MultiBiomeProvider;

import java.util.HashMap;
import java.util.Map;

public class BiomeProviders {
	public static final Registry<BiomeProvider> REGISTRY = new SimpleRegistry<>(
		RegistryKey.ofRegistry(StationAPI.MODID.id("biome_providers")), Lifecycle.stable()
	);
	
	private static Map<Identifier, BiomeProvider> overworldProviders = new HashMap<>();
	private static Map<Identifier, BiomeProvider> netherProviders = new HashMap<>();
	private static MultiBiomeProvider overworldProvider;
	private static MultiBiomeProvider netherProvider;
	private static boolean initiated;
	
	public static void addOverworldProvider(Identifier id, BiomeProvider provider) {
		if (!REGISTRY.containsId(id)) {
			throw new RuntimeException("Biome provider " + id + " must be registered first!");
		}
		overworldProviders.put(id, provider);
	}
	
	public static BiomeProvider getOverworldProvider(long seed) {
		if (overworldProvider == null || overworldProvider.getSeed() != seed) {
			/*if (!initiated) {
				StationAPI.EVENT_BUS.post(new BiomeProviderRegistryEvent());
				initiated = true;
			}*/
			overworldProvider = new MultiBiomeProvider(seed, overworldProviders);
		}
		return overworldProvider;
	}
}
