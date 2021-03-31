package net.modificationstation.stationapi.api.common.event;

import com.google.common.collect.ObjectArrays;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.jodah.typetools.TypeResolver;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class EventBus {

    public EventBus() {
        this(Short.MAX_VALUE);
    }

    public EventBus(int eventTypesBufferSize) {
        listeners = new ListenerContainer[eventTypesBufferSize][0];
        //noinspection unchecked
        listenerRegistries = new Consumer[eventTypesBufferSize];
    }

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
                register(eventType, ListenerExecutorFactory.create(listener, method, eventType), priority);
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
        int globalId = EVENT_ID_LOOKUP.computeIfAbsent(eventType, aClass -> {
            try {
                return eventType.getDeclaredField("ID").getInt(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        if (listeners[globalId] == null)
            listeners[globalId] = new ListenerContainer[0];
        ListenerContainer[] listenerContainers = listeners[globalId];
        //noinspection unchecked
        listenerContainers = ObjectArrays.concat(listenerContainers, new ListenerContainer((Consumer<Event>) listener, priority));
        Arrays.sort(listenerContainers);
        listeners[globalId] = listenerContainers;
        ListenerContainer[] finalListenerContainers = listenerContainers;
        //noinspection unchecked
        listenerRegistries[globalId] =
                listenerContainers.length == 1 ?
                        (Consumer<Event>) listener :
                        listenerContainers.length == 2 ?
                                event ->
                                {
                                    finalListenerContainers[0].invoker.accept(event);
                                    finalListenerContainers[1].invoker.accept(event);
                                } :
                                listenerRegistryFactory.create(listenerContainers);
    }

    @CanIgnoreReturnValue
    public @NotNull <T extends Event> T post(@NotNull final T event) {
        final Consumer<Event> invoker = listenerRegistries[event.getEventID()];
        if (invoker == null) {
            if (!(event instanceof DeadEvent))
                post(new DeadEvent(event));
        } else
            invoker.accept(event);
        return event;
    }

    protected final ListenerRegistryFactory listenerRegistryFactory = new ListenerRegistryFactory(this);
    protected final ListenerContainer[][] listeners;
    protected final Consumer<Event>[] listenerRegistries;

    protected static final Map<Class<? extends Event>, Integer> EVENT_ID_LOOKUP = new IdentityHashMap<>();
}
