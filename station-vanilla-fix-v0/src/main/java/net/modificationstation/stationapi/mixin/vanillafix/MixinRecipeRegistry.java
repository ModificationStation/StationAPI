package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.block.BlockBase;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC,
                    ordinal = 0
            )
    )
    private BlockBase getWoolForStringToWoolRecipe() {
        return Blocks.WHITE_WOOL;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyArg(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 42
            ),
            index = 1
    )
    private Object[] getWoolForPaintingRecipe(Object[] objects) {
        objects[6] = Identifier.of("wools/");
        return objects;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyArg(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 56
            ),
            index = 1
    )
    private Object[] getWoolForBedRecipe(Object[] objects) {
        objects[3] = Identifier.of("wools/");
        return objects;
    }
}
