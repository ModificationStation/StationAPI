package net.modificationstation.stationapi.mixin.recipe.dev;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("UnusedMixin") // Added in a mixin plugin only in development environment with AlwaysMoreItems present
@Mixin(StationShapelessRecipe.class)
public abstract class StationShapelessRecipeMixin {
    @Shadow
    public abstract ItemStack getOutput();

    @Unique
    public ItemStack method_2073() {
        return this.getOutput();
    }
}
