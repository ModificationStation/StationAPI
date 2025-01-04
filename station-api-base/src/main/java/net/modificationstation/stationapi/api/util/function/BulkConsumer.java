package net.modificationstation.stationapi.api.util.function;

import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;

public interface BulkConsumer<A> {
    @Contract(pure = true)
    static <A> BulkConsumer<A> of(Consumer<A> sink) {
        interface BulkConsumerImpl<A> extends BulkConsumer<A> {
            @Contract(pure = true)
            private static <A> BulkConsumerImpl<A> of(Consumer<A> sink) {
                return () -> sink;
            }

            Consumer<A> getSink();

            @Override
            default BulkConsumer<A> accept(A a) {
                getSink().accept(a);
                return this;
            }
        }
        return BulkConsumerImpl.of(sink);
    }

    BulkConsumer<A> accept(A a);
}
