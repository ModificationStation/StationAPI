package net.modificationstation.stationloader.api.common.event.recipe;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

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

    enum Type {

        CRAFTING
    }

    Event<RecipeRegister> EVENT = EventFactory.INSTANCE.newEvent(RecipeRegister.class, (listeners) ->
            (type) -> {
        for (RecipeRegister event : listeners)
            event.registerRecipes(type);
    });

    void registerRecipes(Type recipeType);
}
