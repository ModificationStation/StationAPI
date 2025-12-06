package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FurnaceBlockEntity.class)
class FurnaceBlockEntityMixin {
    @Shadow
    private ItemStack[] inventory;

    @Redirect(
            method = {
                    "canAcceptRecipeOutput",
                    "craftRecipe"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/SmeltingRecipeManager;craft(I)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack stationapi_getResult(SmeltingRecipeManager smeltingRecipeRegistry, int i) {
        return SmeltingRegistry.getResultFor(inventory[0]);
    }

    @Inject(
            method = "getFuelTime",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_getCustomBurnTime(ItemStack arg, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(FuelRegistry.getFuelTime(arg));
    }

    @Inject(
            method = "craftRecipe",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_captureLocals(CallbackInfo ci, ItemStack var1) {
        capturedItemStack = var1;
    }

    @Unique
    private ItemStack capturedItemStack;

    @ModifyConstant(
            method = "craftRecipe",
            constant = @Constant(
                    intValue = 1,
                    ordinal = 0
            )
    )
    private int stationapi_modifyStackIncrement(int constant) {
        return capturedItemStack.count;
    }

    @Inject(
            method = "canAcceptRecipeOutput",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;count:I",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_captureLocals2(CallbackInfoReturnable<Boolean> cir, ItemStack var1) {
        capturedItemStack2ElectricBoogaloo = var1;
    }

    @Unique
    private ItemStack capturedItemStack2ElectricBoogaloo;

    @Redirect(
            method = "canAcceptRecipeOutput",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;count:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int stationapi_fixOverstack(ItemStack stack) {
        return stack.count + capturedItemStack2ElectricBoogaloo.count - 1;
    }
}
