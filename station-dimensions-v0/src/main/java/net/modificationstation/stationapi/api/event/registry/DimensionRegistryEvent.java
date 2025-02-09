package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class DimensionRegistryEvent extends RegistryEvent.EntryTypeBound<DimensionContainer<?>, DimensionRegistry> {
    public DimensionRegistryEvent() {
        super(DimensionRegistry.INSTANCE);
    }
}
