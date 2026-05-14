package net.modificationstation.stationapi.api.tag;

import net.modificationstation.stationapi.api.util.context.Condition;
import net.modificationstation.stationapi.api.util.context.Context;

import java.util.Collection;

public record TagMatchGroup<T>(
        Collection<T> baseItems,
        Collection<Condition<?>> conditions
) {
    public boolean test(T item, Context ctx) {
        if (!baseItems.contains(item)) return false;
        for (Condition<?> condition : conditions) if (!condition.test(ctx)) return false;
        return true;
    }
}
