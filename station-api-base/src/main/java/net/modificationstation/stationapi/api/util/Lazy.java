package net.modificationstation.stationapi.api.util;

import com.google.common.base.Suppliers;

import java.util.function.Supplier;

/**
 * @deprecated Use {@link Suppliers#memoize(com.google.common.base.Supplier)} instead.
 */
@Deprecated
public class Lazy<T> {
    private Supplier<T> supplier;
    private T value;

    public Lazy(Supplier<T> delegate) {
        this.supplier = delegate;
    }

    public T get() {
        Supplier<T> supplier = this.supplier;
        if (supplier != null) {
            this.value = supplier.get();
            this.supplier = null;
        }

        return this.value;
    }
}
