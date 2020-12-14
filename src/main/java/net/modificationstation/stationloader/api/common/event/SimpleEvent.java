package net.modificationstation.stationloader.api.common.event;

import com.google.common.eventbus.EventBus;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationloader.api.common.StationLoader;

import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleEvent<T> extends Event<T> {

    @SuppressWarnings("UnstableApiUsage")
    public static final EventBus EVENT_BUS = new EventBus(StationLoader.INSTANCE.getModID().getContainer().getMetadata().getName() + "_SimpleEvent");

    public SimpleEvent(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper) {
        super(type, eventFunc, listenerWrapper);
    }

    public SimpleEvent(Class<T> type, Function<T[], T> eventFunc) {
        super(type, eventFunc);
    }

    public SimpleEvent(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper, Consumer<SimpleEvent<T>> postProcess) {
        this(type, eventFunc, listenerWrapper);
        postProcess.accept(this);
    }

    public SimpleEvent(Class<T> type, Function<T[], T> eventFunc, Consumer<SimpleEvent<T>> postProcess) {
        this(type, eventFunc);
        postProcess.accept(this);
    }

    public void register(T listener) {
        super.register0(listener);
    }

    @Override
    public void register(EntrypointContainer<T> container) {
        register(container.getEntrypoint());
    }

    public static class Data<T> extends Event.Data<T, SimpleEvent<T>> {

        protected Data(SimpleEvent<T> event) {
            super(event);
        }
    }
}
