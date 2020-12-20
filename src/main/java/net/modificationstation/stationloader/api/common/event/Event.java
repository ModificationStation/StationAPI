package net.modificationstation.stationloader.api.common.event;

import lombok.experimental.SuperBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.util.Instance;
import net.modificationstation.stationloader.impl.common.util.ReflectionHelper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Event class
 *
 * @param <T>
 * @author mine_diver
 **/

public abstract class Event<T> {

    public static Event<?>[] EVENTS = new Event[0];
    private final Class<T> type;
    private final Function<T[], T> eventFunc;
    private final Function<T, T> listenerWrapper;
    private final Map<T, T> unwrappedToWrapped = new HashMap<>();
    private final Map<T, T> wrappedToUnwrapped = new HashMap<>();
    protected T invoker;
    private T[] handlers;

    public Event(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper) {
        this.type = type;
        this.eventFunc = eventFunc;
        this.listenerWrapper = listenerWrapper;
        update();
        EVENTS = Arrays.copyOf(EVENTS, EVENTS.length + 1);
        EVENTS[EVENTS.length - 1] = this;
    }

    public Event(Class<T> type, Function<T[], T> eventFunc) {
        this(type, eventFunc, listener -> listener);
    }

    public Event(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper, Consumer<Event<T>> postProcess) {
        this(type, eventFunc, listenerWrapper);
        postProcess.accept(this);
    }

    public Event(Class<T> type, Function<T[], T> eventFunc, Consumer<Event<T>> postProcess) {
        this(type, eventFunc);
        postProcess.accept(this);
    }

    public final Class<T> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    void update() {
        if (handlers == null)
            invoker = eventFunc.apply((T[]) Array.newInstance(type, 0));
        else if (handlers.length == 1)
            invoker = handlers[0];
        else
            invoker = eventFunc.apply(handlers);
    }

    @SuppressWarnings("unchecked")
    protected T register0(T listener) {
        T wrapped = listenerWrapper.apply(listener);
        unwrappedToWrapped.put(listener, wrapped);
        wrappedToUnwrapped.put(wrapped, listener);
        listener = wrapped;
        if (handlers == null) {
            handlers = (T[]) Array.newInstance(type, 1);
            handlers[0] = listener;
        } else {
            handlers = Arrays.copyOf(handlers, handlers.length + 1);
            handlers[handlers.length - 1] = listener;
        }
        update();
        return listener;
    }

    public abstract void register(EntrypointContainer<T> container);

    public void register(Identifier identifier) {
        FabricLoader.getInstance().getEntrypointContainers(identifier.toString(), type).forEach(container -> {
            register(container);
            T listener = container.getEntrypoint();
            try {
                ReflectionHelper.setFinalFieldsWithAnnotation(listener.getClass(), listener, Instance.class, listener);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public final T wrap(T unwrapped) {
        return unwrappedToWrapped.get(unwrapped);
    }

    public final T unwrap(T wrapped) {
        return wrappedToUnwrapped.get(wrapped);
    }

    public final boolean isWrapped(T listener) {
        return wrappedToUnwrapped.containsKey(listener);
    }

    public final T getInvoker() {
        return invoker;
    }

    @SuperBuilder
    @lombok.Data
    protected static class Data<T, U extends Event<T>> {

        private final U event;

        protected Data(U event) {
            this.event = event;
        }

        public final U getEvent() {
            return event;
        }
    }
}
