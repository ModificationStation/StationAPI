package net.modificationstation.stationapi.api.common.event;

import net.jodah.typetools.TypeResolver;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
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
        if (eventTypes.contains(eventType)) {
            List<ListenerContainer> listenerContainers = listeners.get(eventTypes.indexOf(eventType));
            //noinspection unchecked
            listenerContainers.add(new ListenerContainer((Consumer<Event>) listener, priority));
            Collections.sort(listenerContainers);
        } else {
            eventTypes.add(eventType);
            int eventId = eventTypes.indexOf(eventType);
            List<ListenerContainer> listenerContainers = new CopyOnWriteArrayList<>();
            listeners.add(eventId, listenerContainers);
            //noinspection unchecked
            listenerContainers.add(new ListenerContainer((Consumer<Event>) listener, priority));
        }
    }

    public final @NotNull <T extends Event> T post(@NotNull final T event) {
        //noinspection unchecked
        final Class<T> eventType = (Class<T>) event.getClass();
        final int eventId = eventTypes.indexOf(eventType);
        if (eventId != -1)
            for (final ListenerContainer listener : listeners.get(eventId))
                listener.invoker.accept(event);
        else if (!(event instanceof DeadEvent))
            post(new DeadEvent(event));
        return event;
    }

    private final List<Class<? extends Event>> eventTypes = new CopyOnWriteArrayList<>();
    private final List<List<ListenerContainer>> listeners = new CopyOnWriteArrayList<>();
}
