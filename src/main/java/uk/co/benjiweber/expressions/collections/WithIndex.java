package uk.co.benjiweber.expressions.collections;

import java.util.ArrayList;
import java.util.List;

public interface WithIndex<T> extends ForwardingList<T> {
    public interface IndexedItemMapper<T,R> {
        R map(T it, int index);
    }
    public interface IndexedItemConsumer<T> {
        void accept(T it, int index);
    }

    default void withIndex(IndexedItemConsumer<T> consumer) {
        for (int i = 0; i < delegate().size(); i++) {
            consumer.accept(delegate().get(i), i);
        }
    }

    default <R> List<R> mapWithIndex(IndexedItemMapper<T,R> mapper) {
        List<R> results = new ArrayList<>();
        for (int i = 0; i < delegate().size(); i++) {
            results.add(mapper.map(delegate().get(i), i));
        }
        return results;
    }
}
