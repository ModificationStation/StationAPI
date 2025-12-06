package net.modificationstation.stationapi.impl.vanillafix.recipe;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.registry.tag.ItemTags;

import java.lang.invoke.MethodHandles;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaFuelItemFixImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerFuel(RecipeRegisterEvent event) {
        if (RecipeRegisterEvent.Vanilla.SMELTING.type() == event.recipeId) {
            for (Block block : Block.BLOCKS)
                if (block != null && block.material == Material.WOOD && Item.ITEMS[block.id] != null)
                    FuelRegistry.addFuelItem(Item.ITEMS[block.id], 300);
            FuelRegistry.addFuelItem(Item.STICK, 100);
            FuelRegistry.addFuelTag(ItemTags.COALS, 1600);
            FuelRegistry.addFuelItem(Item.LAVA_BUCKET, 20000);
            FuelRegistry.addFuelTag(ItemTags.SAPLINGS, 100);
        }
    }
}
