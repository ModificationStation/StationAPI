package net.modificationstation.stationloader.api.common.event.recipe;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

/**
 * Event called after one of vanilla recipes system got initialized (CRAFTING_TABLE, FURNACE, etc)
 *
 * args: RecipeRegister.Type
 * return: void
 *
 * @author mine_diver
 *
 */

public interface RecipeRegister {

    enum Vanilla {

        CRAFTING_SHAPED,
        CRAFTING_SHAPELESS,
        SMELTING;

        public String type() {
            return modid + ":" + name().toLowerCase();
        }

        public static Vanilla fromType(String type) {
            for (Vanilla recipe : values())
                if (recipe.type().equals(type))
                    return recipe;
            return null;
        }

        private static final String modid = "minecraft";
    }

    Event<RecipeRegister> EVENT = EventFactory.INSTANCE.newEvent(RecipeRegister.class, listeners ->
            type -> {
        for (RecipeRegister event : listeners)
            event.registerRecipes(type);
    });

    void registerRecipes(String recipeType);
}
