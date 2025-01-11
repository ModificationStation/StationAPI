package net.modificationstation.stationapi.api.event.effect;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EnitityEffectRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class EffectRegistryEvent extends RegistryEvent<EnitityEffectRegistry> {
	public EffectRegistryEvent() {
		super(EnitityEffectRegistry.INSTANCE);
	}
}
