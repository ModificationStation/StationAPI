package net.modificationstation.stationapi.api.common.event;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.core.util.ObjectArrayIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

@RequiredArgsConstructor
public class ListenerList<T extends Event> implements Iterable<ListenerContainer<T>> {

    @SuppressWarnings("unchecked")
    private ListenerContainer<T>[] LISTENER_CACHE = new ListenerContainer[0];

    public final Class<T> type;

    @NotNull
    @Override
    public Iterator<ListenerContainer<T>> iterator() {
        return new ObjectArrayIterator<>(LISTENER_CACHE);
    }

    public void add(ListenerContainer<T> container) {
        LISTENER_CACHE = Arrays.copyOf(LISTENER_CACHE, LISTENER_CACHE.length + 1);
        LISTENER_CACHE[LISTENER_CACHE.length - 1] = container;
        Arrays.sort(LISTENER_CACHE);
    }
}
