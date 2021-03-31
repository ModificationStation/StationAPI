package net.modificationstation.stationapi.mixin.common.accessor;

import net.minecraft.recipe.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.*;

@Mixin(ShapelessRecipe.class)
public interface ShapelessRecipeAccessor {
    @Accessor
    List<?> getInput();
}
