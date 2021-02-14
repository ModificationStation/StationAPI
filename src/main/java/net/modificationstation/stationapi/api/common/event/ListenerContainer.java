package net.modificationstation.stationapi.api.common.event;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ListenerContainer<T extends Event> implements Comparable<ListenerContainer<?>> {

    public static final int DEFAULT_PRIORITY = 0;

    public final Class<T> eventType;
    public final Consumer<T> invoker;
    public final int priority;

    @Override
    public int compareTo(@NotNull ListenerContainer<?> o) {
        return Integer.compare(o.priority, priority);
    }
}
