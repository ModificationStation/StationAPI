package net.modificationstation.stationapi.api.lookup;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.lookup.item.ItemAPILookupEvent;

public final class ItemAPILookup extends EventBasedAPILookup<ItemStack> {

    public ItemAPILookup() {
        super(ItemAPILookupEvent::new);
    }

}
