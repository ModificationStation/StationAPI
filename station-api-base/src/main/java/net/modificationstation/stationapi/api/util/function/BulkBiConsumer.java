package net.modificationstation.stationapi.api.util.function;

import org.jetbrains.annotations.Contract;

import java.util.function.BiConsumer;

public interface BulkBiConsumer<A, B> {
    @Contract(pure = true)
    static <A, B> BulkBiConsumer<A, B> of(BiConsumer<A, B> sink) {
        interface BulkBiConsumerImpl<A, B> extends BulkBiConsumer<A, B> {
            @Contract(pure = true)
            private static <A, B> BulkBiConsumerImpl<A, B> of(BiConsumer<A, B> sink) {
                return () -> sink;
            }

            BiConsumer<A, B> getSink();

            @Override
            default BulkBiConsumer<A, B> accept(A a, B b) {
                getSink().accept(a, b);
                return this;
            }
        }
        return BulkBiConsumerImpl.of(sink);
    }

    BulkBiConsumer<A, B> accept(A a, B b);
}
