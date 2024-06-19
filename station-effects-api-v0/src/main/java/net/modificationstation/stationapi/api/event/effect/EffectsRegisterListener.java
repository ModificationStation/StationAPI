package net.modificationstation.stationapi.api.event.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.impl.effect.EffectAddRemovePacket;
import net.modificationstation.stationapi.impl.effect.EffectRemoveAllPacket;

public class EffectsRegisterListener {
	@EventListener(phase = InitEvent.POST_INIT_PHASE)
	public void onInit(InitEvent event) {
		EffectAddRemovePacket.register();
		EffectRemoveAllPacket.register();
		StationAPI.EVENT_BUS.post(EffectRegistryEvent.builder().build());
	}
}
