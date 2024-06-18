package net.modificationstation.stationapi.api.event.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

public class EffectsRegisterListener {
	@EventListener(phase = InitEvent.POST_INIT_PHASE)
	public void onInit(InitEvent event) {
		StationAPI.EVENT_BUS.post(EffectRegistryEvent.builder().build());
	}
}
