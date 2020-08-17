package net.modificationstation.stationloader.impl.common.event;

import net.modificationstation.stationloader.api.common.event.Event;

import java.util.function.Function;

public class EventFactory implements net.modificationstation.stationloader.api.common.event.EventFactory {

    @Override
    public void setHandler(net.modificationstation.stationloader.api.common.event.EventFactory handler) {
        throw new RuntimeException("You shouldn't be able to access this!");
    }

    @Override
    public <T> Event<T> newEvent(Class<T> type, Function<T[], T> eventFunc) {
        return new StationEvent<T>(type, eventFunc);
    }
}
