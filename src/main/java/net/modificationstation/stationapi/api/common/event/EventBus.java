package net.modificationstation.stationapi.api.common.event;

import net.jodah.typetools.TypeResolver;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

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
                method.setAccessible(true);
                MethodHandles.Lookup lookup = IMPL_LOOKUP.in(method.getDeclaringClass());
                MethodHandle methodHandle;
                try {
                    methodHandle = lookup.unreflect(method);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Impossible error!", e);
                }
                boolean isStatic = Modifier.isStatic(method.getModifiers());
                if (!isStatic && listener == null)
                    throw new IllegalArgumentException(String.format("Method %s#%s isn't static, but no listener instance is provided!", method.getDeclaringClass().getName(), method.getName()));
                try {
                    MethodHandle factory = LambdaMetafactory.metafactory(lookup, "accept", isStatic ? MethodType.methodType(Consumer.class) : MethodType.methodType(Consumer.class, listener.getClass()), MethodType.methodType(void.class, Object.class), methodHandle, MethodType.methodType(void.class, eventType)).getTarget();
                    if (!isStatic)
                        factory = factory.bindTo(listener);
                    //noinspection unchecked
                    register(eventType, (Consumer<T>) factory.invoke(), priority);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
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
        register(new ListenerContainer<>(eventType, listener, EventListener.DEFAULT_PRIORITY));
    }

    public <T extends Event> void register(Class<T> eventType, Consumer<T> listener, int priority) {
        //noinspection unchecked
        ListenerContainer<T>[] listenerContainers = (ListenerContainer<T>[]) listeners.getOrDefault(eventType, new ListenerContainer[0]);
        listenerContainers = Arrays.copyOf(listenerContainers, listenerContainers.length + 1);
        listenerContainers[listenerContainers.length - 1] = new ListenerContainer<>(eventType, listener, priority);
        Arrays.sort(listenerContainers);
        listeners.put(eventType, listenerContainers);
    }

    public <T extends Event> T post(T event) {
        //noinspection unchecked
        Class<T> eventType = (Class<T>) event.getClass();
        if (listeners.containsKey(eventType))
            //noinspection unchecked
            for (ListenerContainer<T> listenerContainer : (ListenerContainer<T>[]) listeners.get(eventType))
                listenerContainer.invoker.accept(event);
        else if (!(event instanceof DeadEvent))
            post(new DeadEvent(event));
        return event;
    }

    private final Map<Class<? extends Event>, ListenerContainer<?>[]> listeners = new IdentityHashMap<>();

    private static final MethodHandles.Lookup IMPL_LOOKUP;
    static {
        try {
            Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            field.setAccessible(true);
            IMPL_LOOKUP = (MethodHandles.Lookup) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
