package net.modificationstation.stationapi.api.event.recipe;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

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

        private static final Map<Identifier, Vanilla> ID_LOOKUP = Util.createIdentityLookupBy(Vanilla::type, values());

        public static @Nullable Vanilla fromType(@NotNull Identifier type) {
            return ID_LOOKUP.get(type);
        }

        @NotNull
        private final Identifier type = of(name().toLowerCase());

        public @NotNull Identifier type() {
            return type;
        }
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
