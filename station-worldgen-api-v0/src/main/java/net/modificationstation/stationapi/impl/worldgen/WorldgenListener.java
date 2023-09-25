package net.modificationstation.stationapi.impl.worldgen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.legacy.PostRegistryRemapEvent;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeProviderRegistryEvent;

public class WorldgenListener {
	private boolean initiated = false;
	
	@EventListener(priority = ListenerPriority.LOWEST)
	public void afterInit(BiomeRegisterEvent event) {
		if (initiated) return;
		StationAPI.EVENT_BUS.post(new BiomeProviderRegistryEvent());
		initiated = false;
	}
	
	@EventListener(priority = ListenerPriority.HIGHEST)
	public void registerBiomes(BiomeProviderRegistryEvent event) {
		event.registerOverworld(StationAPI.MODID.id("overworld_biome_provider"), OverworldBiomeProvider.getInstance());
	}
}
