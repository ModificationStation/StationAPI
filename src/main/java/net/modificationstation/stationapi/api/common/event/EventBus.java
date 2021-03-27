package net.modificationstation.stationapi.api.common.event;

import net.jodah.typetools.TypeResolver;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class EventBus {

    public <T> void register(Class<T> listenerClass) {
        register(listenerClass, null);
    }

    public <T> void register(T listener) {
        register(listener.getClass(), listener);
    }

    public <T> void register(Class<? extends T> listenerClass, T listener) {
        for (Method method : listenerClass.getDeclaredMethods())
            if (method.isAnnotationPresent(EventListener.class) && ((listener == null) == Modifier.isStatic(method.getModifiers()))) {
                EventListener eventListener = method.getAnnotation(EventListener.class);
                ListenerPriority listenerPriority = eventListener.priority();
                register(method, listener, listenerPriority.custom ? eventListener.numPriority() : listenerPriority.numPriority);
            }
    }

    public void register(Method method) {
        register(method, null);
    }

    public void register(Method method, int priority) {
        register(method, null, priority);
    }

    public void register(Method method, Object listener) {
        register(method, listener, EventListener.DEFAULT_PRIORITY);
    }

    public <T extends Event> void register(Method method, Object listener, int priority) {
        if (method.getParameterCount() == 1) {
            Class<?> rawEventType = method.getParameterTypes()[0];
            if (Event.class.isAssignableFrom(rawEventType)) {
                @SuppressWarnings("unchecked") Class<T> eventType = (Class<T>) rawEventType;
                register(
                        eventType,
                        Modifier.isPublic(method.getModifiers()) && Modifier.isPublic(method.getDeclaringClass().getModifiers()) && Modifier.isPublic(method.getParameterTypes()[0].getModifiers()) ?
                                ASMFactory.create(listener, method, eventType) :
                                LambdaFactory.create(listener, method, eventType),
                        priority);
            } else
                throw new IllegalArgumentException(String.format("Method %s#%s's parameter type (%s) can't be assigned to the current EventBus's event type (%s)!", method.getDeclaringClass().getName(), method.getName(), rawEventType.getName(), Event.class.getName()));
        } else
            throw new IllegalArgumentException(String.format("Method %s#%s has %s annotation, but has wrong amount of parameters!", method.getDeclaringClass().getName(), method.getName(), EventListener.class.getName()));
    }

    public <T extends Event> void register(Consumer<T> listener) {
        register(listener, EventListener.DEFAULT_PRIORITY);
    }

    public <T extends Event> void register(Consumer<T> listener, int priority) {
        //noinspection unchecked
        register((Class<T>) TypeResolver.resolveRawArgument(Consumer.class, listener.getClass()), listener, priority);
    }

    public <T extends Event> void register(Class<T> eventType, Consumer<T> listener) {
        register(eventType, listener, EventListener.DEFAULT_PRIORITY);
    }

    public <T extends Event> void register(Class<T> eventType, Consumer<T> listener, int priority) {
        int eventId = idOf(eventTypes, eventType);
        if (eventId == -1) {
            eventTypes = Arrays.copyOf(eventTypes, eventTypes.length + 1);
            eventId = eventTypes.length - 1;
            eventTypes[eventId] = eventType;
            //noinspection unchecked
            ListenerContainer[] listenerContainers = new ListenerContainer[] { new ListenerContainer((Consumer<Event>) listener, priority) };
            listeners = Arrays.copyOf(listeners, listeners.length + 1);
            listeners[eventId] = listenerContainers;
        } else {
            ListenerContainer[] listenerContainers = listeners[eventId];
            listenerContainers = Arrays.copyOf(listenerContainers, listenerContainers.length + 1);
            //noinspection unchecked
            listenerContainers[listenerContainers.length - 1] = new ListenerContainer((Consumer<Event>) listener, priority);
            Arrays.sort(listenerContainers);
            listeners[eventId] = listenerContainers;
        }
    }

    public final @NotNull <T extends Event> T post(@NotNull final T event) {
        final int eventId = idOf(eventTypes, event.getClass());
        if (eventId != -1)
            for (final ListenerContainer listener : listeners[eventId])
                listener.invoker.accept(event);
        else if (!(event instanceof DeadEvent))
            post(new DeadEvent(event));
        return event;
    }

    private static int idOf(Class<? extends Event>[] eventTypes, Class<? extends Event> eventType) {
        for (int i = 0; i < eventTypes.length; i++)
            if (eventType == eventTypes[i])
                return i;
        return -1;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Event>[] eventTypes = new Class[0];
    private ListenerContainer[][] listeners = new ListenerContainer[0][0];
}
