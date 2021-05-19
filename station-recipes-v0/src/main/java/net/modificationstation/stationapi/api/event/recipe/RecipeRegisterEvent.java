package net.modificationstation.stationapi.api.event.recipe;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.jetbrains.annotations.NotNull;

/**
 * Event that allows mods to listen for any type of recipe being registered.
 * Can be used for custom recipe types.
 * @see Vanilla
 * @author mine_diver
 */
@RequiredArgsConstructor
public class RecipeRegisterEvent extends Event {

    public final Identifier recipeId;

    /**
     * Vanilla recipe types as enums for registering through a switchcase.
     */
    public enum Vanilla {

        CRAFTING_SHAPED,
        CRAFTING_SHAPELESS,
        SMELTING;

        private static final ModID modID = ModID.of("minecraft");

        public static @NotNull Vanilla fromType(@NotNull Identifier type) {
            if (modID.equals(type.modID)) return valueOf(type.id.toUpperCase());
            throw new IllegalArgumentException("Tried to get a vanilla recipe enum from an identifier but the modid wasn't \"minecraft\"!");
        }

        public @NotNull Identifier type() {
            return Identifier.of(modID, name().toLowerCase());
        }
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
