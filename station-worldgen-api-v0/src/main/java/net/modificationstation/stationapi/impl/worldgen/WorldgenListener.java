package net.modificationstation.stationapi.impl.worldgen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.event.worldgen.biome.BiomeProviderRegisterEvent;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;

public class WorldgenListener {
	private boolean initiated;
	
	@EventListener(priority = ListenerPriority.LOWEST)
	public void afterInit(BiomeRegisterEvent event) {
		if (initiated) return;
		StationAPI.EVENT_BUS.post(new BiomeProviderRegisterEvent());
		initiated = true;
	}
	
	@EventListener(phase = StationAPI.INTERNAL_PHASE)
	public void registerBiomes(BiomeProviderRegisterEvent event) {
		BiomeAPI.addOverworldBiomeProvider(
			StationAPI.MODID.id("overworld_biome_provider"),
			OverworldBiomeProviderImpl.getInstance()
		);
		BiomeAPI.addNetherBiomeProvider(
			StationAPI.MODID.id("nether_biome_provider"),
			NetherBiomeProviderImpl.getInstance()
		);
	}
}
