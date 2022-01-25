package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.impl.recipe.tag.TagConversionStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.Optional;

import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.SMELTING;
import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.valueOf;

@Mixin(SmeltingRecipeRegistry.class)
public class MixinSmeltingRecipeRegistry {

    @Mutable
    @Shadow
    @Final
    private static SmeltingRecipeRegistry INSTANCE;

    @ModifyArg(method = "addSmeltingRecipe", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 0)
    public Object mine(Object itemId) {
        Optional<Identifier> identifier = ItemRegistry.INSTANCE.getIdentifier((Integer) itemId);
        if (identifier.isPresent()) {
            BiTuple<Identifier, Integer> biTuple = BiTuple.of(identifier.get(), 0);
            if (TagConversionStorage.CONVERSION_TABLE.containsKey(biTuple)) {
                return TagConversionStorage.CONVERSION_TABLE.get(biTuple);
            }
        }
        StationAPI.LOGGER.warn("Couldn't find tag conversion for " + itemId + ". Are you adding recipes via ID?!");
        return itemId;
    }

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void afterRecipeRegister(CallbackInfo ci) {
        INSTANCE = (SmeltingRecipeRegistry) (Object) this;
        StationAPI.EVENT_BUS.post(new RecipeRegisterEvent(SMELTING.type()));
    }
}
