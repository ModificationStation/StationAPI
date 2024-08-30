package net.modificationstation.stationapi.api.lookup.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.lookup.EventBasedAPILookup;
import net.modificationstation.stationapi.api.util.API;

import java.util.Optional;
import java.util.function.Function;

public final class ItemAPILookup {

    private final EventBasedAPILookup<ItemStack> ITEM_STACK_API_LOOKUP  = new EventBasedAPILookup<>(ItemAPILookupEvent::new);

    @API
    public <T> Optional<T> find(Class<T> api, ItemStack stack) {
        return ITEM_STACK_API_LOOKUP.find(api, stack);
    }

    @API
    public <T> Function<ItemStack, Optional<T>> finder(Class<T> api) {
        return stack -> this.find(api, stack);
    }

}
