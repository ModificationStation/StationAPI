package net.modificationstation.stationapi.api.tag;

import net.modificationstation.stationapi.api.util.context.Condition;

import java.util.Collection;

public record TagMatchGroup<T>(
        Collection<T> baseItems,
        Collection<Condition<?>> conditions,
        boolean remove
) {}
