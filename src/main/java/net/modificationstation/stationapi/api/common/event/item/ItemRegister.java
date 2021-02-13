package net.modificationstation.stationapi.api.common.event.item;

import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;

public class ItemRegister extends RegistryEvent<ItemRegistry> {

    public ItemRegister() {
        super(ItemRegistry.INSTANCE);
    }
}
