package net.modificationstation.stationloader.impl.common.event;

import net.modificationstation.stationloader.api.common.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EventFactory implements net.modificationstation.stationloader.api.common.event.EventFactory {

    @Override
    @SuppressWarnings("rawtypes")
    public <T extends Event<?>> void addEvent(Class<T> eventClass, BiFunction<Class<?>, Function, T> factory) {
        factories.put(eventClass, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, U extends Event<T>> U newEvent(Class<U> event, Class<T> type, Function<T[], T> eventFunc) {
        return (U) factories.get(event).apply(type, eventFunc);
    }

    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends Event<?>>, BiFunction<Class<?>, Function, ? extends Event<?>>> factories = new HashMap<>();
}
