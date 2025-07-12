package net.modificationstation.stationapi.api.event.effect;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class EffectRegistryEvent extends RegistryEvent.EntryTypeBound<EntityEffectType<?>, EntityEffectTypeRegistry> {
    public EffectRegistryEvent() {
        super(EntityEffectTypeRegistry.INSTANCE);
    }
}
