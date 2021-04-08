package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.*;

@Mixin(SmeltingRecipeRegistry.class)
public interface SmeltingRecipeRegistryAccessor {

    @Accessor
    Map<Object, ItemInstance> getRecipes();
}
