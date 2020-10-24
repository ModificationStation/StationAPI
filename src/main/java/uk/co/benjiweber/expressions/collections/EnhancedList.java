package uk.co.benjiweber.expressions.collections;

import java.util.List;

import static java.util.Arrays.asList;

public interface EnhancedList<T> extends ForwardingList<T>, WithIndex<T> {

    static <T> EnhancedList<T> enhancedList(T... values) {
        return enhance(asList(values));
    }

    static <T> EnhancedList<T> enhance(List<T> list) {
        return () -> list;
    }

}
