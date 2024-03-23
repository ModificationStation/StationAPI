package net.modificationstation.stationapi.impl.vanillafix.recipe;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.registry.tag.ItemTags;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaFuelItemFixImpl {
    @EventListener
    private static void registerFuel(RecipeRegisterEvent event) {
        if (RecipeRegisterEvent.Vanilla.SMELTING.type() == event.recipeId) {
            for (Block block : Block.BLOCKS)
                if (block != null && block.material == Material.WOOD && Item.ITEMS[block.id] != null)
                    FuelRegistry.addFuelItem(new ItemStack(Item.ITEMS[block.id], 1), 300);
            FuelRegistry.addFuelItem(new ItemStack(Item.STICK, 1), 100);
            FuelRegistry.addFuelTag(ItemTags.COALS, 1600);
            FuelRegistry.addFuelItem(new ItemStack(Item.LAVA_BUCKET, 1), 20000);
            FuelRegistry.addFuelItem(new ItemStack(Block.SLAB.asItem(), 1, 0), 100);
            FuelRegistry.addFuelTag(ItemTags.SAPLINGS, 100);
        }
    }
}
