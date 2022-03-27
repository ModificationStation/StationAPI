package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationapi.api.item.Fuel;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TileEntityFurnace.class)
public class MixinTileEntityFurnace {

    @Shadow
    private ItemInstance[] inventory;

    @Redirect(method = {"canAcceptRecipeOutput()Z", "craftRecipe()V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/SmeltingRecipeRegistry;getResult(I)Lnet/minecraft/item/ItemInstance;"))
    private ItemInstance getResult(SmeltingRecipeRegistry smeltingRecipeRegistry, int i) {
        return SmeltingRegistry.getResultFor(inventory[0]);
    }

    @Inject(method = "getFuelTime(Lnet/minecraft/item/ItemInstance;)I", at = @At(value = "HEAD"), cancellable = true)
    private void getCustomBurnTime(ItemInstance arg, CallbackInfoReturnable<Integer> cir) {
        if (arg != null && arg.getType() instanceof Fuel fuel)
            cir.setReturnValue(fuel.getFuelTime(arg));
    }

    @Inject(method = "craftRecipe()V", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemInstance;count:I", opcode = Opcodes.GETFIELD, ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureLocals(CallbackInfo ci, ItemInstance var1) {
        capturedItemInstance = var1;
    }

    private ItemInstance capturedItemInstance;

    @ModifyConstant(method = "craftRecipe()V", constant = @Constant(intValue = 1, ordinal = 0))
    private int modifyStackIncrement(int constant) {
        return capturedItemInstance.count;
    }

    @Inject(method = "canAcceptRecipeOutput()Z", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemInstance;count:I", opcode = Opcodes.GETFIELD, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureLocals2(CallbackInfoReturnable<Boolean> cir, ItemInstance var1) {
        capturedItemInstance2ElectricBoogaloo = var1;
    }

    private ItemInstance capturedItemInstance2ElectricBoogaloo;

    @Redirect(method = "canAcceptRecipeOutput()Z", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemInstance;count:I", opcode = Opcodes.GETFIELD))
    private int fixOverstack(ItemInstance itemInstance) {
        return itemInstance.count + capturedItemInstance2ElectricBoogaloo.count - 1;
    }
}
