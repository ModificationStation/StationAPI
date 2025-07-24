package net.modificationstation.stationapi.api.event.effect;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class EntityEffectTypeRegistryEvent extends RegistryEvent.EntryTypeBound<EntityEffectType<?>, EntityEffectTypeRegistry> {
    public EntityEffectTypeRegistryEvent() {
        super(EntityEffectTypeRegistry.INSTANCE);
    }
}
