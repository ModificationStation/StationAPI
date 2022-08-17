package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.impl.recipe.SmeltingRegistryImpl;
import net.modificationstation.stationapi.mixin.recipe.SmeltingRecipeRegistryAccessor;

public final class SmeltingRegistry {

    @API
    public static void addSmeltingRecipe(int input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @API
    public static void addSmeltingRecipe(ItemInstance input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @API
    public static ItemInstance getResultFor(ItemInstance input) {
        RegistryEntry<ItemBase> entry = ItemRegistry.INSTANCE.getEntry(ItemRegistry.INSTANCE.getKey(input.getType()).orElseThrow()).orElseThrow();
        for (Object o : ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().keySet())
            //noinspection unchecked
            if (o instanceof ItemInstance item && input.isDamageAndIDIdentical(item) || o instanceof TagKey<?> tag && entry.isIn((TagKey<ItemBase>) tag))
                return ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().get(o);
        return SmeltingRecipeRegistry.getInstance().getResult(input.getType().id);
    }

    @API
    public static int getFuelTime(ItemInstance itemInstance) {
        if (SmeltingRegistryImpl.getWarcrimes() == null)
            throw new RuntimeException("Accessed Lnet/modificationstation/stationapi/api/recipe/SmeltingRegistry;getFuelTime(Lnet/minecraft/item/ItemInstance;)I too early!");
        else
            return SmeltingRegistryImpl.getWarcrimes().invokeGetFuelTime(itemInstance);
    }

    @API
    public static void addFuelTag(Identifier tag, int fuelTime) {
        SmeltingRegistryImpl.TAG_FUEL_TIME.put(TagKey.of(ItemRegistry.INSTANCE.getKey(), tag), fuelTime);
    }
}
