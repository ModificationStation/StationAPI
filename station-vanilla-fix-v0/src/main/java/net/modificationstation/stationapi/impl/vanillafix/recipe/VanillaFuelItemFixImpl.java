package net.modificationstation.stationapi.impl.vanillafix.recipe;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.vanillafix.tag.ItemTags;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaFuelItemFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerFuel(RecipeRegisterEvent event) {
        if (RecipeRegisterEvent.Vanilla.SMELTING.type() == event.recipeId) {
            SmeltingRegistry.addFuelTag(ItemTags.SAPLINGS.id(), 100);
            SmeltingRegistry.addFuelTag(ItemTags.LOGS.id(), 300);
            SmeltingRegistry.addFuelTag(ItemTags.COALS.id(), 1600);
        }
    }
}
