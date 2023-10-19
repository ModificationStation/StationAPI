package net.modificationstation.stationapi.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityFurnace.class)
class MixinTileEntityFurnace {
    @Redirect(
            method = {
                    "canAcceptRecipeOutput()Z",
                    "craftRecipe()V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/SmeltingRecipeRegistry;getResult(I)Lnet/minecraft/item/ItemInstance;"
            )
    )
    private ItemInstance getResult(SmeltingRecipeRegistry smeltingRecipeRegistry, int i) {
        //noinspection DataFlowIssue
        return SmeltingRegistry.getResult((TileEntityFurnace) (Object) this);
    }

    @Inject(
            method = "getFuelTime(Lnet/minecraft/item/ItemInstance;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getCustomBurnTime(ItemInstance arg, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(FuelRegistry.getFuelTime(arg));
    }

    @ModifyConstant(
            method = "craftRecipe()V",
            constant = @Constant(
                    intValue = 1,
                    ordinal = 0
            )
    )
    private int modifyStackIncrement(
            int constant,
            @Local(index = 1) ItemInstance itemInstance
    ) {
        return itemInstance.count;
    }

    @ModifyExpressionValue(
            method = "canAcceptRecipeOutput()Z",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;count:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int fixOverstack(
            int count,
            @Local(index = 1) ItemInstance itemInstance
    ) {
        return count + itemInstance.count - 1;
    }
}
