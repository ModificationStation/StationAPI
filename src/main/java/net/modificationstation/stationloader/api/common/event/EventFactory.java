package net.modificationstation.stationloader.api.common.event;

import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.util.function.Function;

public interface EventFactory extends HasHandler<EventFactory> {

    EventFactory INSTANCE = new EventFactory() {

        private EventFactory handler;

        @Override
        public void setHandler(EventFactory handler) {
            this.handler = handler;
        }

        @Override
        public <T> Event<T> newEvent(Class<T> type, Function<T[], T> eventFunc) {
            checkAccess(handler);
            return handler.newEvent(type, eventFunc);
        }
    };

    <T> Event<T> newEvent(Class<T> type, Function<T[], T> eventFunc);
}
