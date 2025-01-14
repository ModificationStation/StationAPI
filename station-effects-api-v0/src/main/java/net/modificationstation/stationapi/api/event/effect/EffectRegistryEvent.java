package net.modificationstation.stationapi.api.event.effect;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EntityEffectRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class EffectRegistryEvent extends RegistryEvent<EntityEffectRegistry> {
    public EffectRegistryEvent() {
        super(EntityEffectRegistry.INSTANCE);
    }
}
