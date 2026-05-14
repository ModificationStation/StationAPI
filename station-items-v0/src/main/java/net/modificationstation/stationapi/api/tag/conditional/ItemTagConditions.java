package net.modificationstation.stationapi.api.tag.conditional;

import net.modificationstation.stationapi.api.util.context.Context;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class ItemTagConditions {
    /**
     * The context key used to evaluate item damage tag conditions.
     * <p>
     * When populating a context to evaluate a tag that may contain item damage conditions,
     * provide the current item damage integer via this key.
     */
    public static final Context.Key<Integer> ITEM_DAMAGE = new Context.Key<>(NAMESPACE.id("item_damage"));

    private ItemTagConditions() {}
}
