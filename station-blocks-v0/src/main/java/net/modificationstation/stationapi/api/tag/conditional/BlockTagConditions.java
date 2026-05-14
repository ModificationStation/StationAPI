package net.modificationstation.stationapi.api.tag.conditional;

import net.modificationstation.stationapi.api.util.context.Context;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class BlockTagConditions {
    /**
     * The context key used to evaluate block metadata tag conditions.
     * <p>
     * When populating a context to evaluate a tag that may contain block metadata conditions,
     * provide the current metadata integer (0-15) via this key.
     */
    public static final Context.Key<Integer> BLOCK_METADATA = new Context.Key<>(NAMESPACE.id("block_metadata"));

    private BlockTagConditions() {}
}
