package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.SMELTING;

@Mixin(SmeltingRecipeManager.class)
class SmeltingRecipeManagerMixin {
    @Mutable
    @Shadow
    @Final
    private static SmeltingRecipeManager INSTANCE;

//    @ModifyArg(method = "addSmeltingRecipe", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 0)
//    public Object mine(Object itemId) {
//        Optional<Identifier> identifier = ItemRegistry.INSTANCE.getIdByLegacyId((Integer) itemId);
//        if (identifier.isPresent()) {
//            BiTuple<Identifier, Integer> biTuple = BiTuple.of(identifier.get(), 0);
//            if (TagConversionStorage.CONVERSION_TABLE.containsKey(biTuple)) {
//                return TagConversionStorage.CONVERSION_TABLE.get(biTuple);
//            }
//        }
//        StationAPI.LOGGER.warn("Couldn't find tag conversion for " + itemId + ". Are you adding recipes via ID?!");
//        return itemId;
//    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void stationapi_afterRecipeRegister(CallbackInfo ci) {
        INSTANCE = (SmeltingRecipeManager) (Object) this;
        StationAPI.EVENT_BUS.post(RecipeRegisterEvent.builder().recipeId(SMELTING.type()).build());
    }
}
