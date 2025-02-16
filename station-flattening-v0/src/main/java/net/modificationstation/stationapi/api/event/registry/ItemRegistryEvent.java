package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class ItemRegistryEvent extends RegistryEvent.EntryTypeBound<Item, ItemRegistry> {
    public ItemRegistryEvent() {
        super(ItemRegistry.INSTANCE);
    }
}
