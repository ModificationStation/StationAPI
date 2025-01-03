package net.modificationstation.sltest.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EffectRegistry;
import net.modificationstation.stationapi.api.event.effect.EffectRegistryEvent;

public class TestEffectListener {
	@EventListener
	public void registerEffects(EffectRegistryEvent event) {
		assert SLTest.NAMESPACE != null;
		EffectRegistry.register(SLTest.NAMESPACE.id("test_effect"), TestPlayerEffect.class);
		EffectRegistry.register(SLTest.NAMESPACE.id("infinity_effect"), TestPlayerInfEffect.class);
		assert StationAPI.LOGGER != null;
		StationAPI.LOGGER.info("Registered Effects!");
	}
}
