package net.modificationstation.stationapi.api.common.event;

import lombok.RequiredArgsConstructor;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class EventBus<T extends Event> {

    private final Class<T> eventType;

    public <U> void register(Class<U> listenerClass) {
        register(listenerClass, null);
    }

    public <U> void register(U listener) {
        register(listener.getClass(), listener);
    }

    public <U> void register(Class<? extends U> listenerClass, U listener) {
        for (Method method : listenerClass.getDeclaredMethods())
            if (method.isAnnotationPresent(EventListener.class) && ((listener == null) == Modifier.isStatic(method.getModifiers())))
                register(method, listener);
    }

    public void register(Method method) {
        register(method, null);
    }

    public void register(Method method, int priority) {
        register(method, null, priority);
    }

    public void register(Method method, Object listener) {
        register(method, listener, EventListenerData.DEFAULT_PRIORITY);
    }

    public <U extends T> void register(Method method, Object listener, int priority) {
        if (method.getParameterCount() == 1) {
            Class<?> rawEventType = method.getParameterTypes()[0];
            if (this.eventType.isAssignableFrom(rawEventType)) {
                @SuppressWarnings("unchecked") Class<U> eventType = (Class<U>) rawEventType;
                method.setAccessible(true);
                MethodHandles.Lookup lookup = MethodHandles.lookup();
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
                    register(eventType, (Consumer<U>) factory.invoke(), priority);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            } else
                throw new IllegalArgumentException(String.format("Method %s#%s's parameter type (%s) can't be assigned to the current EventBus's event type (%s)!", method.getDeclaringClass().getName(), method.getName(), rawEventType.getName(), this.eventType.getName()));
        } else
            throw new IllegalArgumentException(String.format("Method %s#%s has %s annotation, but has wrong amount of parameters!", method.getDeclaringClass().getName(), method.getName(), EventListener.class.getName()));
    }

//    public <T extends EventNew> void register(Consumer<T> listener) {
//
//    }

    public <U extends T> void register(Class<U> eventType, Consumer<U> listener, int priority) {
        listeners.add(new EventListenerData<>(eventType, listener, priority));
    }

    public <U extends T> void post(U event) {
        //noinspection unchecked
        listeners.stream().filter(eventListenerData -> eventListenerData.eventType.isInstance(event)).forEach(eventListenerData -> ((Consumer<U>) eventListenerData.invoker).accept(event));
    }

    private final Set<EventListenerData<? extends T>> listeners = new TreeSet<>();
}
