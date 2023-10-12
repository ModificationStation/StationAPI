package net.modificationstation.stationapi.api.util.collection;

import org.jetbrains.annotations.Nullable;

public interface IndexedIterable<T> extends Iterable<T> {
    int ABSENT_RAW_ID = -1;

    int getRawId(T var1);

    @Nullable T get(int var1);

    /**
     * {@return the value at {@code index}}
     * 
     * @throws IllegalArgumentException if the value is {@code null}
     */
    default T getOrThrow(int index) {
        T object = this.get(index);
        if (object == null) {
            throw new IllegalArgumentException("No value with id " + index);
        }
        return object;
    }

    int size();
}

