package net.modificationstation.stationapi.api.worldgen.biome;

import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class BiomeProviderRegisterEvent extends Event {
	public BiomeProviderRegisterEvent() {}
}