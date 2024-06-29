package net.modificationstation.stationapi.impl.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.effect.EffectRegistryEvent;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.impl.effect.packet.EffectAddRemovePacket;
import net.modificationstation.stationapi.impl.effect.packet.EffectRemoveAllPacket;
import net.modificationstation.stationapi.impl.effect.packet.SendAllEffectsPacket;
import net.modificationstation.stationapi.impl.effect.packet.SendAllEffectsPlayerPacket;

public class EffectsRegisterListener {
	@EventListener(phase = InitEvent.POST_INIT_PHASE)
	public void onInit(InitEvent event) {
		EffectAddRemovePacket.register();
		EffectRemoveAllPacket.register();
		SendAllEffectsPacket.register();
		SendAllEffectsPlayerPacket.register();
		StationAPI.EVENT_BUS.post(EffectRegistryEvent.builder().build());
	}
}
