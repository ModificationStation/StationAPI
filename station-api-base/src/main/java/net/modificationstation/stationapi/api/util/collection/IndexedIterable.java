package net.modificationstation.stationapi.api.util.collection;

import org.jetbrains.annotations.Nullable;

public interface IndexedIterable<T> extends Iterable<T> {
   int getRawId(T entry);

   @Nullable
   T get(int index);
}
