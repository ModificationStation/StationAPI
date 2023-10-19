package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.RecipeTypeRegistry;

@EventPhases(StationAPI.INTERNAL_PHASE)
public final class RecipeTypeRegistryEvent extends RegistryEvent<RecipeTypeRegistry> {
    public RecipeTypeRegistryEvent() {
        super(RecipeTypeRegistry.INSTANCE);
    }
}
