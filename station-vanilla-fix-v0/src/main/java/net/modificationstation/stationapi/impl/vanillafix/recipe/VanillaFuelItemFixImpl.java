package net.modificationstation.stationapi.impl.vanillafix.recipe;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.vanillafix.tag.ItemTags;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaFuelItemFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerFuel(RecipeRegisterEvent event) {
        if (RecipeRegisterEvent.Vanilla.SMELTING.type() == event.recipeId) {
            for (BlockBase block : BlockBase.BY_ID)
                if (block != null && block.material == Material.WOOD && ItemBase.byId[block.id] != null)
                    FuelRegistry.addFuelItem(ItemBase.byId[block.id], 300);
            FuelRegistry.addFuelItem(ItemBase.stick, 100);
            FuelRegistry.addFuelTag(ItemTags.COALS, 1600);
            FuelRegistry.addFuelItem(ItemBase.lavaBucket, 20000);
            FuelRegistry.addFuelTag(ItemTags.SAPLINGS, 100);
        }
    }
}
