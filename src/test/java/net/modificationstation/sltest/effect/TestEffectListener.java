package net.modificationstation.sltest.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.effect.EffectRegistryEvent;
import net.modificationstation.stationapi.api.registry.Registry;

public class TestEffectListener {
	@EventListener
	public void registerEffects(EffectRegistryEvent event) {
		Registry.register(event.registry, SLTest.NAMESPACE.id("test_effect"), TestPlayerEffect::new);
		Registry.register(event.registry, SLTest.NAMESPACE.id("infinity_effect"), TestPlayerInfEffect::new);
		StationAPI.LOGGER.info("Registered Entity Effects!");
	}
}
