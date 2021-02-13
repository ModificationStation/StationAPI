package net.modificationstation.stationapi.api.common.event.recipe;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;

/**
 * Event called after one of vanilla recipes system got initialized (CRAFTING_TABLE, FURNACE, etc)
 * <p>
 * args: RecipeRegister.Type
 * return: void
 *
 * @author mine_diver
 */
@RequiredArgsConstructor
public class RecipeRegister extends Event {

    public final Identifier recipeId;

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
}
