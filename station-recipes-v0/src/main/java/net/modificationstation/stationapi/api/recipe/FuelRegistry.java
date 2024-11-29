package net.modificationstation.stationapi.api.recipe;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.API;

import java.util.Map;
import java.util.OptionalInt;

public class FuelRegistry {

    private static final Reference2IntMap<TagKey<Item>> TAG_FUEL_TIME = new Reference2IntOpenHashMap<>();
    /**
     * Item > Metadata, which returns the smelt time.
     */
    private static final Reference2ObjectMap<Item, Int2IntMap> ITEM_FUEL_TIME = new Reference2ObjectOpenHashMap<>();

    private static final Reference2IntMap<ItemStack> FUELS = new Reference2IntOpenHashMap<>();
    private static final Reference2IntMap<ItemStack> FUELS_VIEW = Reference2IntMaps.unmodifiable(FUELS);
    private static boolean viewInvalidated;

    @API
    public static int getFuelTime(ItemStack stack) {
        if (stack == null)
            return 0;

        // First see if a tag matches, and if so, use that
        OptionalInt foundKey = stack.getRegistryEntry().streamTags().mapToInt(TAG_FUEL_TIME::getInt).filter(value -> value > 0).findFirst();
        if (foundKey.isPresent())
            return foundKey.getAsInt();

        // Then check if the item has a specific entry
        int fuelTime = ITEM_FUEL_TIME.get(stack.getItem()).getOrDefault(stack.getDamage(), 0);
        if (fuelTime != 0)
            return fuelTime;

        // Finally check if there's a wildcard
        return ITEM_FUEL_TIME.get(stack.getItem()).getOrDefault(-1, 0);
    }

    @API
    public static void addFuelTag(TagKey<Item> tag, int fuelTime) {
        viewInvalidated = true;
        TAG_FUEL_TIME.put(tag, fuelTime);
    }

    @API
    public static void addFuelItem(Item item, int fuelTime) {
        addFuelItem(item, -1, fuelTime);
    }

    @API
    public static void addFuelItem(Item item, int metadata, int fuelTime) {
        viewInvalidated = true;
        ITEM_FUEL_TIME.computeIfAbsent(item, (o) -> new Int2IntOpenHashMap()).put(metadata, fuelTime);
    }

    @API
    public static Reference2IntMap<ItemStack> getFuelsView() {
        if (viewInvalidated) {
            FUELS.clear();
            for (Reference2IntMap.Entry<TagKey<Item>> entry : TAG_FUEL_TIME.reference2IntEntrySet())
                for (RegistryEntry<Item> item : ItemRegistry.INSTANCE.getOrCreateEntryList(entry.getKey()))
                    FUELS.put(new ItemStack(item.value()), entry.getIntValue());
            for (Map.Entry<Item, Int2IntMap> item2Meta2FuelValues : ITEM_FUEL_TIME.reference2ObjectEntrySet())
                for (Int2IntMap.Entry meta2FuelValues : item2Meta2FuelValues.getValue().int2IntEntrySet())
                    FUELS.put(new ItemStack(item2Meta2FuelValues.getKey(), 1, meta2FuelValues.getIntKey()), meta2FuelValues.getIntValue());
            viewInvalidated = false;
        }
        return FUELS_VIEW;
    }
}
