package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class ItemRegistryEvent extends RegistryEvent<ItemRegistry> {
    public ItemRegistryEvent() {
        super(ItemRegistry.INSTANCE);
    }
}
