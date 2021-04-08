package net.modificationstation.stationapi.api.event.recipe;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

/**
 * Event called after one of vanilla recipes system got initialized (CRAFTING_TABLE, FURNACE, etc)
 * <p>
 * args: RecipeRegister.Type
 * return: void
 *
 * @author mine_diver
 */
@RequiredArgsConstructor
public class RecipeRegisterEvent extends Event {

    public final Identifier recipeId;

    public enum Vanilla {

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

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
