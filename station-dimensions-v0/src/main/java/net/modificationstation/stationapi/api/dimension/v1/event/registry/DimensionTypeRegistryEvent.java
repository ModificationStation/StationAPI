package net.modificationstation.stationapi.api.dimension.v1.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

@EventPhases(StationAPI.INTERNAL_PHASE)
public final class DimensionTypeRegistryEvent extends RegistryEvent.Logical<DimensionType<?>, DimensionTypeRegistry> {
    public DimensionTypeRegistryEvent() {
        super(DimensionTypeRegistry.INSTANCE);
    }
}
