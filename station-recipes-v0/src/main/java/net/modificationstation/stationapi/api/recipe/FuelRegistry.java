package net.modificationstation.stationapi.api.recipe;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.API;

public class FuelRegistry {

    private static final Reference2IntMap<TagKey<Item>> TAG_FUEL_TIME = new Reference2IntOpenHashMap<>();
    private static final Reference2IntMap<ItemStack> ITEM_FUEL_TIME = new Reference2IntOpenHashMap<>();

    private static final Reference2IntMap<ItemStack> FUELS = new Reference2IntOpenHashMap<>();
    private static final Reference2IntMap<ItemStack> FUELS_VIEW = Reference2IntMaps.unmodifiable(FUELS);
    private static boolean viewInvalidated;

    @API
    public static int getFuelTime(ItemStack stack) {
        return stack == null ? 0 : stack.getRegistryEntry().streamTags().mapToInt(TAG_FUEL_TIME::getInt).filter(value -> value > 0).findFirst().orElseGet(() -> ITEM_FUEL_TIME.getInt(stack.getItem()));
    }

    @API
    public static void addFuelTag(TagKey<Item> tag, int fuelTime) {
        viewInvalidated = true;
        TAG_FUEL_TIME.put(tag, fuelTime);
    }

    @API
    public static void addFuelItem(ItemStack item, int fuelTime) {
        viewInvalidated = true;
        ITEM_FUEL_TIME.put(item, fuelTime);
    }

    @API
    public static Reference2IntMap<ItemStack> getFuelsView() {
        if (viewInvalidated) {
            FUELS.clear();
            for (Reference2IntMap.Entry<TagKey<Item>> entry : TAG_FUEL_TIME.reference2IntEntrySet()) for (RegistryEntry<Item> item : ItemRegistry.INSTANCE.getOrCreateEntryList(entry.getKey())) FUELS.put(new ItemStack(item.value()), entry.getIntValue());
            for (Reference2IntMap.Entry<ItemStack> entry : ITEM_FUEL_TIME.reference2IntEntrySet()) FUELS.put(entry.getKey(), entry.getIntValue());
            viewInvalidated = false;
        }
        return FUELS_VIEW;
    }
}
