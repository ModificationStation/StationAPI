package net.modificationstation.stationapi.api.recipe;

import lombok.val;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.mixin.recipe.SmeltingRecipeRegistryAccessor;

import java.util.Map;

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
    public static void addSmeltingRecipe(TagKey<ItemBase> input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    /**
     * @deprecated Use {@link #getResult(TileEntityFurnace)} instead.
     */
    @API
    @Deprecated
    public static ItemInstance getResultFor(ItemInstance input) {
        for (Map.Entry<Object, ItemInstance> entry : ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().entrySet()) {
            Object o = entry.getKey();
            //noinspection unchecked,ConstantConditions
            if (o instanceof ItemInstance item && input.isDamageAndIDIdentical(item) || o instanceof TagKey<?> tag && input.isIn((TagKey<ItemBase>) tag))
                return entry.getValue();
        }
        return SmeltingRecipeRegistry.getInstance().getResult(input.getType().id);
    }

    @API
    public static ItemInstance getResult(TileEntityFurnace inventory) {
        val input = inventory.getInventoryItem(0);
        for (Map.Entry<Object, ItemInstance> entry : ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().entrySet()) {
            Object o = entry.getKey();
            //noinspection unchecked,ConstantConditions
            if (o instanceof ItemInstance item && input.isDamageAndIDIdentical(item) || o instanceof TagKey<?> tag && input.isIn((TagKey<ItemBase>) tag))
                return entry.getValue();
            if (o instanceof StationSmeltingRecipe recipe && recipe.stationapi_matches(inventory))
                return recipe.stationapi_craft(inventory);
        }
        return SmeltingRecipeRegistry.getInstance().getResult(input.getType().id);

    }
}
