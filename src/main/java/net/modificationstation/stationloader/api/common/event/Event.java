package net.modificationstation.stationloader.api.common.event;

import lombok.experimental.SuperBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationloader.api.common.registry.Identifier;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Event class
 * 
 * @author mine_diver
 *
 * @param <T>
 **/

public abstract class Event<T> {

    protected T invoker;
    private T[] handlers;
    private final Class<T> type;
    private final Function<T[], T> eventFunc;
    private final Function<T, T> listenerWrapper;
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
        listener = listenerWrapper.apply(listener);
        if (handlers == null) {
            handlers = (T[]) Array.newInstance(type, 1);
            handlers[0] = listener;
        }
        else {
            handlers = Arrays.copyOf(handlers, handlers.length + 1);
            handlers[handlers.length - 1] = listener;
        }
        update();
        return listener;
    }

    public abstract void register(EntrypointContainer<T> container);

    public void register(Identifier identifier) {
        FabricLoader.getInstance().getEntrypointContainers(identifier.toString(), type).forEach(this::register);
    }

    public final T getInvoker() {
        return invoker;
    }

    public static Event<?>[] EVENTS = new Event[0];

    @SuperBuilder
    @lombok.Data
    protected static class Data<T, U extends Event<T>> {

        protected Data(U event) {
            this.event = event;
        }

        public final U getEvent() {
            return event;
        }

        private final U event;
    }
}
