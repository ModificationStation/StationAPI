package net.modificationstation.stationapi.api.recipe;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.item.StationItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.API;

public class FuelRegistry {

    private static final Reference2IntMap<TagKey<ItemBase>> TAG_FUEL_TIME = new Reference2IntOpenHashMap<>();
    private static final Reference2IntMap<ItemBase> ITEM_FUEL_TIME = new Reference2IntOpenHashMap<>();

    private static final Reference2IntMap<ItemInstance> FUELS = new Reference2IntOpenHashMap<>();
    private static final Reference2IntMap<ItemInstance> FUELS_VIEW = Reference2IntMaps.unmodifiable(FUELS);
    private static boolean viewInvalidated;

    @API
    public static int getFuelTime(ItemInstance itemInstance) {
        return itemInstance == null ? 0 : StationItemStack.class.cast(itemInstance).getRegistryEntry().streamTags().mapToInt(TAG_FUEL_TIME::getInt).filter(value -> value > 0).findFirst().orElseGet(() -> ITEM_FUEL_TIME.getInt(itemInstance.getType()));
    }

    @API
    public static void addFuelTag(TagKey<ItemBase> tag, int fuelTime) {
        viewInvalidated = true;
        TAG_FUEL_TIME.put(tag, fuelTime);
    }

    @API
    public static void addFuelItem(ItemBase item, int fuelTime) {
        viewInvalidated = true;
        ITEM_FUEL_TIME.put(item, fuelTime);
    }

    @API
    public static Reference2IntMap<ItemInstance> getFuelsView() {
        if (viewInvalidated) {
            FUELS.clear();
            for (Reference2IntMap.Entry<TagKey<ItemBase>> entry : TAG_FUEL_TIME.reference2IntEntrySet()) for (RegistryEntry<ItemBase> item : ItemRegistry.INSTANCE.getOrCreateEntryList(entry.getKey())) FUELS.put(new ItemInstance(item.value()), entry.getIntValue());
            for (Reference2IntMap.Entry<ItemBase> entry : ITEM_FUEL_TIME.reference2IntEntrySet()) FUELS.put(new ItemInstance(entry.getKey()), entry.getIntValue());
            viewInvalidated = false;
        }
        return FUELS_VIEW;
    }
}
