package net.modificationstation.stationloader.api.common.factory;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface EventFactory extends HasHandler<EventFactory> {

    EventFactory INSTANCE = new EventFactory() {

        private EventFactory handler;

        @Override
        public void setHandler(EventFactory handler) {
            this.handler = handler;
        }

        @Override
        @SuppressWarnings("rawtypes")
        public <T extends Event<?>> void addEvent(Class<T> eventClass, BiFunction<Class<?>, Function, T> factory) {
            checkAccess(handler);
            handler.addEvent(eventClass, factory);
        }

        @Override
        public <T, U extends Event<T>> U newEvent(Class<U> event, Class<T> type, Function<T[], T> eventFunc) {
            checkAccess(handler);
            return handler.newEvent(event, type, eventFunc);
        }
    };

    @SuppressWarnings("rawtypes")
    <T extends Event<?>> void addEvent(Class<T> eventClass, BiFunction<Class<?>, Function, T> factory);

    @SuppressWarnings("unchecked")
    default <T> Event<T> newEvent(Class<T> type, Function<T[], T> eventFunc) {
        return newEvent(Event.class, type, eventFunc);
    }

    @SuppressWarnings("unchecked")
    default <T> ModIDEvent<T> newModIDEvent(Class<T> type, Function<T[], T> eventFunc) {
        return newEvent(ModIDEvent.class, type, eventFunc);
    }

    <T, U extends Event<T>> U newEvent(Class<U> event, Class<T> type, Function<T[], T> eventFunc);
}
