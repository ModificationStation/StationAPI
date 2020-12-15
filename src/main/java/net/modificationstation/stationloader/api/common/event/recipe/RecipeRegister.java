package net.modificationstation.stationloader.api.common.event.recipe;

import lombok.Getter;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.ModID;

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

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<RecipeRegister> EVENT = new SimpleEvent<>(RecipeRegister.class,
            listeners ->
                    recipeId -> {
                        for (RecipeRegister listener : listeners)
                            listener.registerRecipes(recipeId);
                    },
            (Consumer<SimpleEvent<RecipeRegister>>) recipeRegister ->
                    recipeRegister.register(recipeId -> SimpleEvent.EVENT_BUS.post(new Data(recipeId)))
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

    final class Data extends SimpleEvent.Data<RecipeRegister> {

        @Getter
        private final Identifier recipeId;

        private Data(Identifier recipeId) {
            super(EVENT);
            this.recipeId = recipeId;
        }
    }
}
