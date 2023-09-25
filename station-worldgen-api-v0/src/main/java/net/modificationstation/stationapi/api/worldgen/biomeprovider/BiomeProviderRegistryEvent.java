package net.modificationstation.stationapi.api.worldgen.biomeprovider;

import net.modificationstation.stationapi.api.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.worldgen.BiomeProviders;

public class BiomeProviderRegistryEvent extends RegistryEvent<Registry<BiomeProvider>> {
	public BiomeProviderRegistryEvent() {
		super(BiomeProviders.REGISTRY);
	}
	
	public void registerOverworld(Identifier id, BiomeProvider provider) {
		Registry.register(registry, id, provider);
		BiomeProviders.addOverworldProvider(id, provider);
	}
}