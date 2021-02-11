package net.modificationstation.stationapi.api.common.event.recipe;

import net.modificationstation.stationapi.api.common.event.GameEventOld;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;

import java.util.function.Consumer;

/**
 * Event called after one of vanilla recipes system got initialized (CRAFTING_TABLE, FURNACE, etc)
 * <p>
 * args: RecipeRegister.Type
 * return: void
 *
 * @author mine_diver
 */

public interface RecipeRegister {

    GameEventOld<RecipeRegister> EVENT = new GameEventOld<>(RecipeRegister.class,
            listeners ->
                    recipeId -> {
                        for (RecipeRegister listener : listeners)
                            listener.registerRecipes(recipeId);
                    },
            (Consumer<GameEventOld<RecipeRegister>>) recipeRegister ->
                    recipeRegister.register(recipeId -> GameEventOld.EVENT_BUS.post(new Data(recipeId)))
    );

    void registerRecipes(Identifier recipeId);

    enum Vanilla {

        CRAFTING_SHAPED,
        CRAFTING_SHAPELESS,
        SMELTING;

        private static final ModID modID = ModID.of("minecraft");

        public static Vanilla fromType(Identifier type) {
            for (Vanilla recipe : values())
                if (recipe.type().equals(type))
                    return recipe;
            return null;
        }

        public Identifier type() {
            return Identifier.of(modID, name().toLowerCase());
        }
    }

    final class Data extends GameEventOld.Data<RecipeRegister> {

        public final Identifier recipeId;

        private Data(Identifier recipeId) {
            super(EVENT);
            this.recipeId = recipeId;
        }
    }
}
