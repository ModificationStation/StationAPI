package net.modificationstation.stationapi.api.lookup.item;

import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.lookup.ApiLookupEvent;

@Cancelable
public class ItemAPILookupEvent extends ApiLookupEvent {

    public final ItemStack itemStack;

    public ItemAPILookupEvent(ItemStack itemStack, Class<?> apiClass) {
        super(apiClass);
        this.itemStack = itemStack;
    }
}