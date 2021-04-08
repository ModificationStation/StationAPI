package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.oredict.OreDict;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.impl.recipe.SmeltingRegistryImpl;
import net.modificationstation.stationapi.mixin.recipe.SmeltingRecipeRegistryAccessor;

public class SmeltingRegistry {

    @API
    public static void addSmeltingRecipe(int input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @API
    public static void addSmeltingRecipe(ItemInstance input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @API
    public static void addOreDictSmeltingRecipe(String input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @API
    public static ItemInstance getResultFor(ItemInstance input) {
        for (Object o : ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().keySet()) {
            if (o instanceof ItemInstance && input.isDamageAndIDIdentical((ItemInstance) o) || o instanceof String && OreDict.INSTANCE.matches((String) o, input))
                return ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().get(o);
        }
        return SmeltingRecipeRegistry.getInstance().getResult(input.getType().id);
    }

    @API
    public static int getFuelTime(ItemInstance itemInstance) {
        if (SmeltingRegistryImpl.getWarcrimes() == null)
            throw new RuntimeException("Accessed Lnet/modificationstation/stationapi/api/common/recipe/SmeltingRegistry;getFuelTime(Lnet/minecraft/item/ItemInstance;)I too early!");
        else
            return SmeltingRegistryImpl.getWarcrimes().invokeGetFuelTime(itemInstance);
    }
}
