package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.RecipeSerializerRegistry;

@EventPhases(StationAPI.INTERNAL_PHASE)
public final class RecipeSerializerRegistryEvent extends RegistryEvent<RecipeSerializerRegistry> {
    public RecipeSerializerRegistryEvent() {
        super(RecipeSerializerRegistry.INSTANCE);
    }
}
