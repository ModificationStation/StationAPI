package net.modificationstation.stationapi.api.common.event;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

@RequiredArgsConstructor
final class ListenerContainer implements Comparable<ListenerContainer> {

    final Consumer<Event> invoker;
    final int priority;

    @Override
    public int compareTo(final @NotNull ListenerContainer o) {
        return Integer.compare(o.priority, priority);
    }
}
