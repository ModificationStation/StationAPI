package net.modificationstation.stationapi.api.lookup.item;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.lookup.ApiLookup;
import net.modificationstation.stationapi.api.util.API;

import java.util.Optional;
import java.util.function.Function;

public final class ItemAPILookup {

    @API
    public <T> Optional<T> find(Class<T> api, ItemStack stack) {
        return ApiLookup.fromEvent(api, new ItemAPILookupEvent(api, stack));
    }

    @API
    public <T> Function<ItemStack, Optional<T>> finder(Class<T> api) {
        return stack -> this.find(api, stack);
    }

}
