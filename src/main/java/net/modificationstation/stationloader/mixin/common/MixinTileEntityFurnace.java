package net.modificationstation.stationloader.mixin.common;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationloader.api.common.item.Fuel;
import net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityFurnace.class)
public class MixinTileEntityFurnace {

    @Shadow
    private ItemInstance[] contents;

    @Redirect(method = {"canAcceptRecipeOutput()Z", "craftRecipe()V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/SmeltingRecipeRegistry;getResult(I)Lnet/minecraft/item/ItemInstance;"))
    private ItemInstance getResult(SmeltingRecipeRegistry smeltingRecipeRegistry, int i) {
        return SmeltingRegistry.INSTANCE.getResultFor(contents[0]);
    }

    @Inject(method = "getFuelTime(Lnet/minecraft/item/ItemInstance;)I", at = @At(value = "HEAD"), cancellable = true)
    private void getCustomBurnTime(ItemInstance arg, CallbackInfoReturnable<Integer> cir) {
        if (arg != null && arg.getType() instanceof Fuel)
            cir.setReturnValue(((Fuel) arg.getType()).getFuelTime(arg));
    }
}
