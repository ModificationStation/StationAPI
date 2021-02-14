package net.modificationstation.stationapi.api.common.event;

import java.util.*;

public class ListenerLists {

    private final Map<Class<? extends Event>, ListenerList<?>> listenerLists = new IdentityHashMap<>();
    private final Map<Class<? extends Event>, Class<? extends Event>[]> parentEventsCache = new IdentityHashMap<>();
    private final Map<Class<? extends Event>, ListenerContainer<?>[]> LISTENERS_CACHE = new IdentityHashMap<>();

    public <T extends Event> ListenerContainer<T>[] getListeners(Class<T> eventType) {
        //noinspection unchecked
        return (ListenerContainer<T>[]) LISTENERS_CACHE.computeIfAbsent(eventType, aClass -> new ListenerContainer[0]);
    }

    public void addListener(ListenerContainer<?> container) {
        Class<? extends Event> eventType = container.eventType;
        if (!parentEventsCache.containsKey(eventType))
            computeParentEvents(eventType);
        Class<? extends Event>[] eventTypes = parentEventsCache.get(eventType);
        for (Class<? extends Event> currentEventType : eventTypes)
            //noinspection unchecked,rawtypes,rawtypes
            listenerLists.get(currentEventType).add((ListenerContainer) container);
    }

    private void computeParentEvents(Class<? extends Event> eventType) {
        Class<? extends Event> currentClass = eventType;
        List<Class<? extends Event>> parentEvents = new ArrayList<>(Collections.singletonList(currentClass));
        while (currentClass != Event.class) {
            //noinspection unchecked
            currentClass = (Class<? extends Event>) currentClass.getSuperclass();
            parentEvents.add(currentClass);
        }
        //noinspection unchecked
        parentEventsCache.put(eventType, parentEvents.toArray(new Class[0]));
    }
}
